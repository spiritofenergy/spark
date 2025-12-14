package com.kodex.spark

import MenuScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kodex.spark.ui.addScreen.AddBookScreen
import com.kodex.spark.ui.addScreen.data.AddScreenObject
import com.kodex.spark.ui.admin_panel.AdminPanelNavObject
import com.kodex.spark.ui.admin_panel.AdminPanelScreen
import com.kodex.spark.ui.admin_panel.ModerationNavObject
import com.kodex.spark.ui.admin_panel.ModerationScreen
import com.kodex.spark.ui.ads.YandexAdsManager
import com.kodex.spark.ui.data.LoginScreenObject
import com.kodex.spark.ui.data.MainScreenDataObject
import com.kodex.spark.ui.detailScreen.ui.DetailScreen
import com.kodex.spark.ui.detailScreen.data.DetailsNavObject
import com.kodex.spark.ui.logon.LoginScreen
import com.kodex.spark.ui.room.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.koin.compose.viewmodel.koinViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var yandexAdsManager: YandexAdsManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Connection()
           val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = LoginScreenObject
            ) {
                composable<LoginScreenObject> {
                    LoginScreen() { navData ->
                        navController.navigate(navData)
                    }
                }
                composable<MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    MenuScreen(
                        navData = navData,
                        onBookClick = {bk ->
                          //  yandexAdsManager.showAd(this@MainActivity){
                                navController.navigate(DetailsNavObject(
                                    bookId = bk.key,
                                    title = bk.title,
                                    description = bk.description,
                                    price = bk.price.toString(),
                                    categoryIndex = bk.categoryIndex,
                                    imageUrl = bk.imageUrl,
                                    author = bk.author,
                                    timestamp = bk.timestamp
                                ))
                      //      }


                        },
                        onBookEditClick = { book->
                            navController.navigate(AddScreenObject(
                                key = book.key,
                                title = book.title,
                                description = book.description,
                                price = book.price,
                                categoryIndex = book.categoryIndex,
                                imageUrl = book.imageUrl,
                                author = book.author,
                                timestamp = book.timestamp
                            ))
                        },
                        onAdminClick = {
                            navController.navigate(AdminPanelNavObject)
                        },
                        onAddBookClick = {
                            navController.navigate(AddScreenObject())
                        },
                    )
                }
                composable<AddScreenObject>{ navEntry ->
                    val navData = navEntry.toRoute<AddScreenObject>()
                    AddBookScreen(
                        navData,
                        onSaved = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<DetailsNavObject>{ navEntry ->
                    val navData = navEntry.toRoute<DetailsNavObject>()
                    DetailScreen(navData)
                }

                composable<AdminPanelNavObject>{
                    AdminPanelScreen(
                        onAddBookClick = {
                            navController.navigate(AddScreenObject())
                        },
                        onModerationClick = {
                            navController.navigate(ModerationNavObject)
                        }
                    )
                }
                composable<ModerationNavObject>{
                    ModerationScreen()
                }
            }
        }
    }
}
@Composable
fun Connection(
    viewModel: RoomViewModel = koinViewModel()
) {
    val listState = viewModel.ratingListFlow.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.insertRating()
        connect()
        disconnected()
        sendData("Sergey")
        receiveData()
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(listState.value) { rating ->

            Text(
                text = "User name ${rating.name}",
            )

        }
    }

}
fun connect(): String{
    val status = "Connect to server b"
    println(status)
    return status
}

fun disconnected(){
    println("Disconnected from server b")
}

fun sendData(data: String){
    println("Send data to server b: $data")
}

fun receiveData(): String {
    return "Receive data from server b"
}
