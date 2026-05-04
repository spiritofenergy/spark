package com.kodex.spark.ui.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kodex.spark.ui.theme.ButtonColor
import com.kodex.spark.ui.theme.ButtonColorDark
import com.kodex.spark.ui.theme.PurpleGrey80

@Composable
fun BottomMenu(
    selectedItem: Int,
    onFavesClick: ()-> Unit = {},
    onHomeClick: ()-> Unit = {},
    onParkClick: ()-> Unit = {},
    onSunnyClick: ()-> Unit = {},
    onFoodClick: ()-> Unit = {},
    onHealthClick: ()-> Unit = {},
    onBookingClick: ()-> Unit = {}
) {
    val items = listOf(
        BottomMenuItem.Home,
       // BottomMenuItem.Faves,
        BottomMenuItem.Park,
        BottomMenuItem.Sunny,
        BottomMenuItem.Food,
        BottomMenuItem.Health,
    )
    NavigationBar (
        containerColor = ButtonColorDark
    ){
        items.forEach{ item->
           NavigationBarItem(
               selected = selectedItem == item.titleId,
               onClick = {
                   // сравниваем значение нажатого элемента и запускаем нужную функцию
                   when(item.titleId){
                       BottomMenuItem.Home.titleId -> onHomeClick()
                       BottomMenuItem.Faves.titleId -> onFavesClick()
                       BottomMenuItem.Park.titleId -> onParkClick()
                       BottomMenuItem.Sunny.titleId -> onSunnyClick()
                       BottomMenuItem.Food.titleId -> onFoodClick()
                       BottomMenuItem.Health.titleId -> onHealthClick()
                       BottomMenuItem.Booking.titleId -> onBookingClick()
                   }
               },
               icon = {
                   Icon(
                       painter = painterResource(id = item.iconId),
                       contentDescription = null)
                      },


               label = {
                   Text(text = stringResource(item.titleId))
               },

               colors = NavigationBarItemDefaults.colors( MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),

                   /*// Цвета для выделенного элемента
                   selectedIconColor = Color.White,
                   selectedTextColor = Color.White,
                   // Цвета для невыделенного элемента
                   unselectedIconColor = Color.Gray,
                   unselectedTextColor = Color.Gray,
                   // Цвет индикатора (если есть)
                   selectedIndicatorColor = ButtonColor*/

               )
           )

        }
    }
}