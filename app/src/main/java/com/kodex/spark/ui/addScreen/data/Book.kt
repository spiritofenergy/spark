package com.kodex.spark.ui.addScreen.data

import com.kodex.spark.ui.utils.Categories

data class Book(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val prise: String = "",
    val categoryIndex: Int = Categories.DRAMA,
    val imageUrl: String = "",
    val isFaves: Boolean = false
)
