package com.kodex.spark.ui.detailScreen

import kotlinx.serialization.Serializable

@Serializable
data class DetailsNavObject(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = ""
)
