package com.kodex.spark.ui.detailScreen.data

data class RatingData (
    val name: String  = "",
    val rating: Int = 0,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)