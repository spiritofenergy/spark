package com.kodex.spark.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.spark.ui.detailScreen.data.RatingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RoomViewModel(private val mainDb: MainDb): ViewModel() {
    val ratingListFlow = mainDb.dao.getFavesFromRoom()

    fun insertRating(){
        viewModelScope.launch(Dispatchers.IO){
           mainDb.dao.insertRating(RatingData(name = "Alex"))
        }
    }
}