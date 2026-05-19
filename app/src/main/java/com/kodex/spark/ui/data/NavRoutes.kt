package com.kodex.spark.ui.data

import com.kodex.spark.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
class NavRoutes {
    @Serializable
    data class MainScreenDataObject(
        val uid: String = "",
        val email: String = ""
    )
    @Serializable
    object PlaceScreenObject
    @Serializable
    data class ParallaxNavObject(
        val bookId: String = "",
        val title: String = "",
        val description: String = "",
        val price: Int = 0,
        val isOpenNow: Boolean = true,
        val openingHours: String = "09:00 - 21:00",
        val address: String = "Кучугуры, ул. Мира, 29",
        val telephone: String = "+7 (918) 460-96-04 ",
        val website: String = "https://iskra-sea.ru/",
        val latitude: String = "9",
        val longitude:String = "21",
        val categoryIndex: Int = Categories.ALL,
        val imageUrl: String = "",
        val author: String = "",
        val timestamp: Long = System.currentTimeMillis(),
        val isFaves: Boolean = false,
        val ratingsList: List<Int> = emptyList()
    )
    @Serializable
    object ModerationScreenObject
    @Serializable
    object AdminPanelNavObject
    @Serializable
    object ModerationNavObject
    @Serializable
    object LoginScreenObject
    @Serializable
    data class DetailNavObject(
        val bookId: String = "",
        val title: String = "",
        val description: String = "",
        val price: Int = 0,
        val telephone: String = "",
        val categoryIndex: Int = Categories.ALL,
        val imageUrl: String = "",
        val author: String = "",
        val timestamp: Long = System.currentTimeMillis(),
        val isFaves: Boolean = false,
        val ratingsList: List<Int> = emptyList()
    )
    fun DetailNavObject.toCommentsNavData(): CommentsNavData {
        return CommentsNavData(
            bookId = bookId,
            title = title,
            ratingsList = ratingsList
        )
    }


    @Serializable
    data class AddScreenObject(
        val key: String = "",
        val title: String = "",
        val description: String = "",
        val price: Int = 0,
        val telephone: String = "",
        val categoryIndex: Int = Categories.ALL,
        val imageUrl: String = "",
        val isFavorite: Boolean = false,
        val isAuthor: Boolean = false,
        val authorId: Int = 0,
        val publishPeriod: Int = 1,
        val timeStamp: Long = System.currentTimeMillis(),
        val deleteDate: String = "",
        val village: String = "",
        val delivery: Boolean = false,
        val ratingsList: List<Int> = emptyList(),
    )

    @Serializable
    data class CommentsNavData (
        val bookId: String = "",
        val title: String = "",
        val ratingsList: List<Int> = emptyList(),

        )

}