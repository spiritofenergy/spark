package com.kodex.spark.ui.placeScreen

data class Review(
    val id: String,
    val userName: String,
    val userAvatar: String?,
    val rating: Float,
    val text: String,
    val date: String,
    val likes: Int

)
