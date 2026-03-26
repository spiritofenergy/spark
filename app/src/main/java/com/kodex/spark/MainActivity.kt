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
import com.kodex.spark.ui.admin_panel.AdminPanelScreen
import com.kodex.spark.ui.admin_panel.ModerationScreen
import com.kodex.spark.ui.ads.YandexAdsManager
import com.kodex.spark.ui.commentsScreen.CommentsScreen
import com.kodex.spark.ui.data.NavRoutes
import com.kodex.spark.ui.detailScreen.ui.DetailScreen
import com.kodex.spark.ui.logon.LoginScreen
import com.kodex.spark.ui.parallaxScreen.ParallaxScreen
import com.kodex.spark.ui.room.RoomViewModel
import com.kodex.spark.ui.utils.Categories
import dagger.hilt.android.AndroidEntryPoint
import org.koin.compose.viewmodel.koinViewModel
import javax.inject.Inject
import kotlin.String

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
                startDestination = NavRoutes.LoginScreenObject
            ) {
                composable<NavRoutes.LoginScreenObject> {
                    LoginScreen() { navData ->
                        navController.navigate(navData)
                    }
                }
                composable<NavRoutes.MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.MainScreenDataObject>()

                    MenuScreen(
                        navData = navData,

                        onBookClick = { bk ->
                            //  yandexAdsManager.showAd(this@MainActivity){
                            navController.navigate(NavRoutes.ParallaxScreenObject(
                                bookId = bk.key,
                                title = bk.title,
                                description = bk.description,
                                price = bk.price,
                                categoryIndex = bk.categoryIndex,
                                imageUrl = bk.imageUrl,
                                author = bk.author,
                                timestamp = bk.timestamp,
                                ratingsList = bk.ratingsList,




                                 )
                            )
                            //      }
                        },

                       /* onBookClick = { bk ->
                          //  yandexAdsManager.showAd(this@MainActivity){
                                navController.navigate(NavRoutes.DetailNavObject(
                                    bookId = bk.key,
                                    title = bk.title,
                                    description = bk.description,
                                    price = bk.price,
                                    categoryIndex = bk.categoryIndex,
                                    imageUrl = bk.imageUrl,
                                    author = bk.author,
                                    timestamp = bk.timestamp,
                                    ratingsList = bk.ratingsList
                                )
                                )
                      //      }
                        },*/
                        onBookEditClick = { book->
                            navController.navigate(NavRoutes.AddScreenObject(
                                key = book.key,
                                title = book.title,
                                description = book.description,
                                price = book.price,
                                categoryIndex = book.categoryIndex,
                                imageUrl = book.imageUrl,
                                timeStamp = book.timestamp
                            ))
                        },
                        onModerationClick = {
                            navController.navigate(NavRoutes.ModerationScreenObject)
                        },
                        onAddBookClick = {
                            navController.navigate(NavRoutes.AddScreenObject())
                        },
                        onLoginClick = {
                            navController.navigate(NavRoutes.LoginScreenObject)
                        }
                    )
                }
                composable<NavRoutes.AddScreenObject>{ navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.AddScreenObject>()
                    AddBookScreen(
                        navData,
                        onSaved = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<NavRoutes.DetailNavObject>{ navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.DetailNavObject>()
                    DetailScreen(
                        onCommentClick = {commentNavData ->
                        navController.navigate(commentNavData)
                    },
                        navObject = navData)
                }
                composable<NavRoutes.ModerationScreenObject> {
                    ModerationScreen()
                }

                composable<NavRoutes.AdminPanelNavObject>{
                    AdminPanelScreen(
                        onAddBookClick = {
                            navController.navigate(NavRoutes.AddScreenObject())
                        },
                        onModerationClick = {
                            navController.navigate(NavRoutes.ModerationNavObject)
                        }
                    )
                }
                    composable<NavRoutes.ModerationNavObject>{
                        ModerationScreen()
                    }

                composable<NavRoutes.ParallaxScreenObject>{navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.ParallaxScreenObject>()
                    ParallaxScreen(
                        navObject = navData,
                        onBackPressed = { navController.popBackStack() },
                        onCallTaxi = { _, _ -> /* Позвонить */ },
                        onNavigateToReviews = {}
                    )
                }

                composable<NavRoutes.CommentsNavData> {navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.CommentsNavData>()
                    CommentsScreen(
                        navObject = navData)
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

           /* Text(
                text = "User name ${rating.name}",
            )*/

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
