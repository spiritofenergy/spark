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
    object Setting : BottomMenuItem(
        route = "",
        titleId = R.string.settings,
        iconId = R.drawable.ic_settings
    )
}