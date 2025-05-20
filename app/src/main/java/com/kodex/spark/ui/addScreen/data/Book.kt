package com.kodex.spark.ui.addScreen.data

import com.kodex.spark.ui.utils.Categories

data class Book(
    val key: String = "",
    val title: String = "",
    val searchTitle: String = title.lowercase(),
    val description: String = "",
    val price: Int = 0,
    val categoryIndex: Int = Categories.ALL,
    val imageUrl: String = "",
    val author: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFaves: Boolean = false
)
