package com.kodex.spark.ui.detailScreen.data

data class RatingData (
    val name: String  = "",
    val uid: String  = "",
    val rating: Int = 1,
    val lastRating: Int = 0,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val bookId: String = ""
)