package com.kodex.spark

import MenuScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kodex.spark.ui.addScreen.AddBookScreen
import com.kodex.spark.ui.addScreen.data.AddScreenObject
import com.kodex.spark.ui.data.LoginScreenObject
import com.kodex.spark.ui.data.MainScreenDataObject
import com.kodex.spark.ui.detailScreen.DetailScreen
import com.kodex.spark.ui.detailScreen.DetailsNavObject
import com.kodex.spark.ui.logon.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                            navController.navigate(DetailsNavObject(
                                title = bk.title,
                                description = bk.description,
                                price = bk.prise,
                                categoryIndex = bk.categoryIndex,
                                imageUrl = bk.imageUrl,
                                isFaves = bk.isFaves
                            ))
                        },

                        onBookEditClick = { book->
                            navController.navigate(AddScreenObject(
                                key = book.key,
                                title = book.title,
                                description = book.description,
                                prise = book.prise,
                                categoryIndex = book.categoryIndex,
                                imageUrl = book.imageUrl,
                                isFaves = book.isFaves

                                )
                            )
                        }
                    )
                    {
                        navController.navigate(AddScreenObject())
                    }
                }
                composable<AddScreenObject>{ navEntry ->
                    val navData = navEntry.toRoute<AddScreenObject>()
                    AddBookScreen(navData){
                        navController.popBackStack()
                    }
                }
                composable<DetailsNavObject>{ navEntry ->
                    val navData = navEntry.toRoute<DetailsNavObject>()
                    DetailScreen(navData)

                }
            }
        }
    }
}

