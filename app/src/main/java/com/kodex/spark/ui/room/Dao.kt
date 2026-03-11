package com.kodex.spark.ui.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.detailScreen.data.RatingData
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM ratingData")
    fun getFavesFromRoom(): Flow<List<RatingData>>

    @Insert
    suspend fun insertRating(ratingData: RatingData)
}