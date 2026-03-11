package com.kodex.spark.ui.room

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
 import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.detailScreen.data.RatingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.kodex.spark.ui.room.Dao
import kotlinx.coroutines.flow.Flow


class RoomViewModel(private val mainDb: MainDb): ViewModel() {
    val ratingListFlow = mainDb.dao.getFavesFromRoom()

    fun insertRating(){
        viewModelScope.launch(Dispatchers.IO){
           mainDb.dao.insertRating(RatingData(name = "Alex"))
        }
    }
}