package com.kodex.spark.ui.detailScreen.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import com.kodex.spark.ui.detailScreen.data.RatingData
import com.kodex.spark.ui.utils.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging
) : ViewModel(){

    val  ratingState  = mutableStateOf("0")
    val  commentState  = mutableStateOf(emptyList<RatingData>())

    fun insertRating(ratingData: RatingData, bookId: String){
        fireStoreManager.insertRating(ratingData, bookId)
    }
    fun getAverageRating(bookId: String) = viewModelScope.launch{
        val ratingPair = fireStoreManager.getRating(bookId)
        ratingState.value = ratingPair.first.toString()
        commentState.value = ratingPair.second
    }
}