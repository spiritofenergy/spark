package com.kodex.spark.ui.addScreen.data

import kotlinx.serialization.Serializable

@Serializable
data class AddScreenObject (
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val prise: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val isFaves: Boolean = false

)