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
    data class ParallaxScreenObject(
        val bookId: String = "",
        val title: String = "",
        val description: String = "",
        val price: Int = 0,
        val isOpenNow: Boolean = true,
        val openingHours: String = "",
        val address: String = "Москва, ул. Тверская, 15",
        val telephone: String = "+7(495)123-45-67",
        val website: String = "coffeehouse.ru",
        val latitude: String = "9",
        val longitude:String = "22",
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