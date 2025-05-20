package com.kodex.spark.ui.bottom_menu
import com.kodex.spark.R

sealed class BottomMenuItem (
    val route: String,
    val titleId: Int,
    val iconId: Int
){
    object Home : BottomMenuItem(
        route = "",
        titleId = R.string.home,
        iconId = R.drawable.ic_home
    )
    object Faves : BottomMenuItem(
        route = "",
        titleId = R.string.faves,
        iconId = R.drawable.ic_favorite
    )
    object Park : BottomMenuItem(
        route = "",
        titleId = R.string.park,
        iconId = R.drawable.ic_park
    )
    object Food : BottomMenuItem(
        route = "",
        titleId = R.string.food,
        iconId = R.drawable.ic_food
    )
    object Health : BottomMenuItem(
        route = "",
        titleId = R.string.health,
        iconId = R.drawable.ic_health
    )
    object Sunny : BottomMenuItem(
        route = "",
        titleId = R.string.sunny,
        iconId = R.drawable.ic_sunny
    )
    object Booking : BottomMenuItem(
        route = "",
        titleId = R.string.booking,
        iconId = R.drawable.ic_booking
    )
}