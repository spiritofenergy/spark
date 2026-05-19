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
import com.kodex.spark.ui.bottom_menu.BottomMenuItem
import com.kodex.spark.ui.theme.ButtonColor
import com.kodex.spark.ui.theme.ButtonColorDark
import com.kodex.spark.ui.theme.ButtonDarkBlue
import com.kodex.spark.ui.theme.DarkBlue
import com.kodex.spark.ui.theme.DrawerColorBlue
import com.kodex.spark.ui.theme.Orange
import com.kodex.spark.ui.theme.TopBatColor

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
        BottomMenuItem.Park,
        BottomMenuItem.Sunny,
        BottomMenuItem.Food,
        BottomMenuItem.Health,
    )

    NavigationBar(containerColor = ButtonColorDark) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item.titleId,
                onClick = {
                    when(item.titleId) {
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
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(item.titleId))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = ButtonDarkBlue,
                    unselectedTextColor = ButtonDarkBlue,
                    indicatorColor = ButtonColor,  // ← вместо selectedIndicatorColor
                    disabledIconColor = TopBatColor.copy(alpha = 0.8f),
                    disabledTextColor = TopBatColor.copy(alpha = 0.8f)
                )
            )
        }
    }
}