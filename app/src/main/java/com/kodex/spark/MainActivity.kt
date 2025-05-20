package com.kodex.spark

import MenuScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kodex.spark.ui.addScreen.AddBookScreen
import com.kodex.spark.ui.addScreen.data.AddScreenObject
import com.kodex.spark.ui.ads.YandexAdsManager
import com.kodex.spark.ui.data.LoginScreenObject
import com.kodex.spark.ui.data.MainScreenDataObject
import com.kodex.spark.ui.detailScreen.ui.DetailScreen
import com.kodex.spark.ui.detailScreen.data.DetailsNavObject
import com.kodex.spark.ui.logon.LoginScreen
import dagger.hilt.android.AndroidEntryPoint
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
                            )
                            )
                        }
                    ){
                        navController.navigate(AddScreenObject())
                    }
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
            }
        }
    }
}

