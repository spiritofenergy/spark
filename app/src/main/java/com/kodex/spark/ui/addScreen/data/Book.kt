package com.kodex.spark.ui.addScreen.data

data class Book(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val prise: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val isFaves: Boolean = false
)
