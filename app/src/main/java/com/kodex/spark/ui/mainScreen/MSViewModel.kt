package com.kodex.spark.ui.mainScreen

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.bottom_menu.BottomMenuItem
import com.kodex.spark.ui.utils.Categories
import com.kodex.spark.ui.utils.FirestoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MSViewModel @Inject constructor(
    private val firebaseManager: FirestoreManager,
) : ViewModel() {
    val booksListState = mutableStateOf(emptyList<Book>())
    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val isFavesListEmptyState = mutableStateOf(false)
    val categoryState = mutableIntStateOf(Categories.ALL)
    var bookToDelete: Book? = null

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) = viewModelScope.launch() {
        _uiState.emit(state)
    }

    fun getAllBooks(){
        categoryState.intValue = Categories.ALL
        sendUiState(MainUiState.Loading)
        firebaseManager.getAllBooks (
            onBooks = { books ->
                    booksListState.value = books
                    isFavesListEmptyState.value = books.isEmpty()
                    sendUiState(MainUiState.Success)
                },
                onFailure = {message ->
                    sendUiState(MainUiState.Error(message))
            }
        )
    }

    fun deleteBook() {
        if (bookToDelete == null) return
        firebaseManager.deleteBook(
            bookToDelete!!,
            onDeleted = {
                booksListState.value = booksListState.value.filter {
                    it.key != bookToDelete!!.key
                }
            },
            onFailure = {
                sendUiState(MainUiState.Error(it))
            }
        )
    }

    fun getAllFavesBooks() {
        categoryState.intValue = Categories.FAVORITES
        sendUiState(MainUiState.Loading)
        firebaseManager.getAllFavesBooks(
            onBooks = { books ->
                booksListState.value = books
                isFavesListEmptyState.value = books.isEmpty()
                sendUiState(MainUiState.Success)
            },
            onFailure = { message ->
                sendUiState(MainUiState.Error(message))
            }
        )
    }

    fun getBooksFromCategory(categoryIndex: Int) {
        sendUiState(MainUiState.Loading)
        categoryState.intValue = categoryIndex
        firebaseManager.getAllBooksFromCategory(
            categoryIndex,
            onBooks = { books ->
                booksListState.value = books
                isFavesListEmptyState.value = books.isEmpty()
                sendUiState(MainUiState.Success)
            },
            onFailure = {message->
                sendUiState(MainUiState.Error(message))
            }
        )
    }

    fun onFavClick(book: Book, isFavState: Int) {
        val booksList = firebaseManager.changeFavState(booksListState.value, book)
        booksListState.value = if (isFavState == BottomMenuItem.Faves.titleId) {
            booksList.filter { it.isFaves }
        } else {
            booksList
        }
        isFavesListEmptyState.value = booksListState.value.isEmpty()
    }

    sealed class MainUiState {
        data object Loading : MainUiState()
        data object Success : MainUiState()
        data class Error(val message: String) : MainUiState()
    }

}