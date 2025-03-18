package com.kodex.spark.ui.addScreen.data

import com.kodex.spark.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
data class AddScreenObject (
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val prise: String = "",
    val categoryIndex: Int = Categories.FANTASY,
    val imageUrl: String = "",
    val isFaves: Boolean = false

)