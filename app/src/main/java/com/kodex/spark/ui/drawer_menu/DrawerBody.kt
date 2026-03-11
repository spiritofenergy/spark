package com.kodex.spark.ui.drawer_menu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.kodex.spark.R
import com.kodex.spark.ui.bottom_menu.BottomMenuItem
import com.kodex.spark.ui.mainScreen.DrawerMenuItem
import com.kodex.spark.ui.mainScreen.MainScreenViewModel
import com.kodex.spark.ui.theme.ButtonColorDark
import com.kodex.spark.ui.theme.DarkTransparentBlue
import com.kodex.spark.ui.theme.GrayLite
import com.kodex.spark.ui.utils.Categories
import com.yandex.mobile.ads.impl.v
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun DrawerBody(
    viewModel: MainScreenViewModel = hiltViewModel(),
    onAdmin: (Boolean) -> Unit,
    onAdminClick: () -> Unit = {},
    onAddBookClick: () -> Unit = {},
    onCategoryClick: (Int) -> Unit = {},
    onLoginClick: () -> Unit = {}

) {

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val categoryList = stringArrayResource(id = R.array.category_arrays)
    val categoryAdmin = stringArrayResource(id = R.array.category_admin)

    val isAdminState = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.isAdmin { isAdmin ->
            isAdminState.value = isAdmin
            onAdmin(isAdmin)
        }
    }

    Box(modifier = Modifier.fillMaxSize()
        .background(ButtonColorDark)
        .padding(20.dp)) {

        Column (modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(6.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GrayLite)
            )
            Spacer(modifier = Modifier.height(16.dp))

            DrawerMenuItem(
                iconDrawableId = Icons.Default.Park,
                text = categoryList[0],
                onItemClick = {
                    onCategoryClick(Categories.PARK)
                    coroutineScope.launch { drawerState.close() }
                }
            )
            DrawerMenuItem(
                iconDrawableId = Icons.Default.WbSunny,
                text = categoryList[1],
                onItemClick = {
                    onCategoryClick(Categories.SUNNY)
                    coroutineScope.launch { drawerState.close() }
                }
            )
            DrawerMenuItem(
                iconDrawableId = Icons.Default.EmojiFoodBeverage,
                text = categoryList[2],
                onItemClick = {
                    onCategoryClick(Categories.FOOD)
                    coroutineScope.launch { drawerState.close() }
                }
            )
            DrawerMenuItem(
                iconDrawableId = Icons.Default.HeartBroken,
                text = categoryList[3],
                onItemClick = {
                    onCategoryClick(Categories.HEALTH)
                    coroutineScope.launch { drawerState.close() }
                }
            )
            DrawerMenuItem(
                iconDrawableId = Icons.Default.CleaningServices,
                text = categoryList[4],
                onItemClick = {
                    onCategoryClick(Categories.SERVICES)
                    coroutineScope.launch { drawerState.close() }
                }
            )
            DrawerMenuItem(
                iconDrawableId = Icons.Default.Bed,
                text = categoryList[5],
                onItemClick = {
                    onCategoryClick(Categories.BOOKING)
                    coroutineScope.launch { drawerState.close() }
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GrayLite))
            Spacer(modifier = Modifier.height(15.dp))



            if (viewModel.isAdminState.value)
                DrawerMenuItem(
                    iconDrawableId = Icons.Default.Security,
                    text = categoryAdmin[0],
                    onItemClick = {
                        viewModel.isAdmin{ }
                        onAdminClick()
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            DrawerMenuItem(
                iconDrawableId = Icons.Default.Add,
                text = categoryAdmin[1],
                onItemClick = {
                    onAdminClick()
                    onAddBookClick
                    coroutineScope.launch { drawerState.close() }
                }
            )
            DrawerMenuItem(
                iconDrawableId = Icons.Default.Input,
                text = categoryAdmin[2],
                onItemClick = {
                    onLoginClick()
                    coroutineScope.launch { drawerState.close() }
                }
            )


            /*  DrawerListItem(title = stringResource(id = R.string.faves)) {
                   onCategoryClick(Categories.FAVORITES)
                }
               DrawerListItem(title = stringResource(id = R.string.all)) {
                   onCategoryClick(Categories.ALL)
                   viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId

               }*/
          /*  LazyColumn(Modifier.fillMaxWidth()) {
                itemsIndexed(categoryList){index, title->
                    DrawerListItem(viewModel,
                        title) {
                            onCategoryClick(index)
                    }
                }
            }*/

            if (isAdminState.value) Button(
                onClick = {
                viewModel.isAdmin{ }
                onAdminClick()
            },
               modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkTransparentBlue
                    )
                ) {
                Text(text = "Admin panel")
            }
            Button(
                onClick = {
               // isAdmin{ }
                onAdminClick()
                    onAddBookClick
            },
               modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkTransparentBlue
                    )
                ) {
                Text(text = "Добавить")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DrawerBodyPreview() {
    DrawerBody(onAdmin = {})
}

