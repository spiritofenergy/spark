package com.kodex.spark.ui.detailScreen

import com.kodex.spark.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
data class DetailsNavObject(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val categoryIndex: Int = Categories.FANTASY,
    val imageUrl: String = "",
    val isFaves: Boolean = false

)
