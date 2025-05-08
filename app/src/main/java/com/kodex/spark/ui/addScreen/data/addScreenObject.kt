package com.kodex.spark.ui.addScreen.data

import com.kodex.spark.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
data class AddScreenObject (
    val key: String = "",
    val title: String = "",
    val searchTitle: String = title.lowercase(),
    val description: String = "",
    val price: Int = 0,
    val categoryIndex: Int = Categories.FANTASY,
    val imageUrl: String = "",
    val isFaves: Boolean = false

)