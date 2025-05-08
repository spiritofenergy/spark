package com.kodex.spark.ui.addScreen

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.spark.ui.addScreen.data.AddScreenObject
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.mainScreen.MainScreenViewModel.MainUiState
import com.kodex.spark.ui.utils.Categories
import com.kodex.spark.ui.utils.FireStoreManagerPaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asSharedFlow

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val firebaseManager: FireStoreManagerPaging,
) : ViewModel() {
    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val prise = mutableStateOf("")
    val selectedCategory = mutableIntStateOf(Categories.FANTASY)
    val selectedImageUri = mutableStateOf<Uri?>(null)
    val showLoadingIndicator =  mutableStateOf(false)

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun setDefaultData(navData: AddScreenObject) {
        title.value = navData.title
        description.value = navData.description
        prise.value = navData.price.toString()
        selectedCategory.intValue = navData.categoryIndex


    }

    fun uploadBook(
        navData: AddScreenObject,
    ) {
        sendUiState(MainUiState.Loading)
        val book = Book(
            key = navData.key,
            title = title.value,
            description = description.value,
            price = prise.value.toInt(),
            categoryIndex = selectedCategory.intValue,
            imageUrl = navData.imageUrl
        )
        firebaseManager.saveBookImage(
            oldImageUrl = navData.imageUrl,
            uri = selectedImageUri.value,
            book = book,
            onSaved = {
                sendUiState(MainUiState.Success)
            },
            onError = { massage ->
                sendUiState(MainUiState.Error(massage))
            }
        )
    }
}