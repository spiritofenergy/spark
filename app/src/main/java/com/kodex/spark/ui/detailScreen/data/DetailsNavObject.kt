package com.kodex.spark.ui.detailScreen.data

import com.kodex.spark.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
data class DetailsNavObject(
    val bookId: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val categoryIndex: Int = Categories.PARK,
    val imageUrl: String = "",
    val author: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFaves: Boolean = false,
    val ratingsList: List<Int> = emptyList()

)
