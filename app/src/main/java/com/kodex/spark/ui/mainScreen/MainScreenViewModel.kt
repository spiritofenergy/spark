package com.kodex.spark.ui.mainScreen

import kotlinx.coroutines.flow.combine

import androidx.paging.map
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.bottom_menu.BottomMenuItem
import com.kodex.spark.ui.utils.Categories
import com.kodex.spark.ui.utils.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firebaseManagerPainter: FireStoreManagerPaging,
    //  private val base64: ImageUtils,
    private val pager: Flow<PagingData<Book>>,
) : ViewModel() {
    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val categoryState = mutableIntStateOf(Categories.ALL)
    var bookToDelete: Book? = null
    var deleteBook = false
    private val booksListUpdate = MutableStateFlow<List<Book>>(emptyList())
    val books: Flow<PagingData<Book>> = pager.cachedIn(viewModelScope)
        .combine(booksListUpdate) { pagingData, booksList ->
            val pgData = pagingData.map { book ->
                val updateBook = booksList.find {
                    it.key == book.key
                }
                updateBook ?: book
            }

            if (deleteBook) {
                deleteBook = false
                pgData.filter { pgBook ->
                    booksList.find {
                        it.key == pgBook.key
                    } != null
                }
            } else {
                pgData
            }
        }


    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()
    private fun sendUiState(state: MainUiState) = viewModelScope.launch() {
        _uiState.emit(state)
    }


    fun deleteBook(uiList: List<Book>) {
        if (bookToDelete == null) return
        firebaseManagerPainter.deleteBook(
            bookToDelete!!,
            onDeleted = {
                deleteBook = true
                booksListUpdate.value = uiList.filter {
                    it.key != bookToDelete!!.key
                }
            },
            onFailure = {
                sendUiState(MainUiState.Error(it))
            }
        )
    }
    fun searchBook(searchText: String){
        firebaseManagerPainter.searchText = searchText
    }
    fun getBooksFromCategory(categoryIndex: Int) {
        categoryState.intValue = categoryIndex
        firebaseManagerPainter.categoryIndex = categoryIndex
    }

    fun onFavClick(book: Book, isFavState: Int, bookList: List<Book>) {
        val booksList = firebaseManagerPainter.changeFavState(bookList, book)
        booksListUpdate.value = if (isFavState == BottomMenuItem.Faves.titleId) {
            booksList.filter { it.isFaves }
        } else {
            booksList
        }

    }

    sealed class MainUiState {
        data object Loading : MainUiState()
        data object Success : MainUiState()
        data class Error(val message: String) : MainUiState()
    }
}