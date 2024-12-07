package com.kodex.spark.ui.bottom_menu
import com.kodex.spark.R

sealed class BottomMenuItem (
    val route: String,
    val title: String,
    val iconId: Int
){
    object Home : BottomMenuItem(
        route = "home",
        title = "Home",
        iconId = R.drawable.ic_home
    )
    object Favorite : BottomMenuItem(
        route = "favorite",
        title = "Favorite",
        iconId = R.drawable.ic_favorite
    )
    object Setting : BottomMenuItem(
        route = "setting",
        title = "Setting",
        iconId = R.drawable.ic_settings
    )
}