import android.annotation.SuppressLint
import android.provider.CalendarContract.Colors
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.bottom_menu.BottomMenu
import com.kodex.spark.ui.bottom_menu.BottomMenuItem
import com.kodex.spark.ui.data.MainScreenDataObject
import com.kodex.spark.ui.drawer_menu.DrawerBody
import com.kodex.spark.ui.drawer_menu.DrawerHeader
import com.kodex.spark.ui.mainScreen.BookListItemUi
import com.kodex.spark.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kodex.spark.ui.custom.FilterDialog
import com.kodex.spark.ui.custom.MyDialog
import com.kodex.spark.ui.mainScreen.MainScreenViewModel
import com.kodex.spark.ui.theme.DarkBlue
import com.kodex.spark.ui.theme.DarkWhite
import com.kodex.spark.ui.theme.Purple80
import com.kodex.spark.ui.theme.PurpleGrey80
import com.kodex.spark.ui.theme.TopBatColorWiete
import com.kodex.spark.ui.top_app_bar.MainTopBar
import com.kodex.spark.ui.utils.Categories


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun MenuScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    navData: MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onBookClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Open)
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val isAuthorState = remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val books = viewModel.books.collectAsLazyPagingItems()

    val isAdminState = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        viewModel.uiState.collect { uiState ->
            if (uiState is MainScreenViewModel.MainUiState.Error) {
                Toast.makeText(context, uiState.massage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    LaunchedEffect(books.loadState.refresh) {
        if (books.loadState.refresh is LoadState.Error) {
            val errorMassage = (books.loadState.refresh as LoadState.Error).error.message
            Toast.makeText(context, errorMassage, Toast.LENGTH_SHORT).show()
        }

    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(navData.email)
                DrawerBody(
                    onAdmin = { isAdmin ->
                        isAdminState.value = isAdmin
                    },
                    onAdminClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onAdminClick()
                        //  viewModel.getBooksFromCategory(Categories.FAVORITES)
                    },
                    onCategoryClick = { categoryIndex ->
                        if (categoryIndex == Categories.PARK) {
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Faves.titleId
                        } else {
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                        }
                        viewModel.getBooksFromCategory(categoryIndex)
                        books.refresh()
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Faves.titleId
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                Row (modifier = Modifier.fillMaxWidth())
                    {
                    if (viewModel.showTopMenu.value == true)
                   /* IconButton(
                        modifier = Modifier.padding(top = 35.dp).background(DarkWhite),
                        onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) { Icon(Icons.Default.Menu,
                        contentDescription = "Burger")
                    }*/

                    MainTopBar(
                        viewModel.categoryState.intValue,
                        onSearch = { searchText ->
                            viewModel.searchBook(searchText)
                            viewModel.showTopMenu.value = true
                            books.refresh()
                        },
                        onTab = {
                            viewModel.showTabOneOrTo.value = viewModel.showTabOneOrTo.value != true
                        },
                      /*  onTopMenu = {
                            viewModel.showTopMenu.value = false
                        },*/
                        onClickTopMenu = {
                            viewModel.showTopMenu.value = true
                        },
                        onFilter = {
                            // showFilterDialog = true
                        },

                        )

                }




            },
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    viewModel.selectedBottomItemState.intValue,
                    onFavesClick = {
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Faves.titleId
                        viewModel.getBooksFromCategory(Categories.PARK)
                        books.refresh()
                    },

                    onHomeClick = {
                        // получаем список с иыентификатором и
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                        viewModel.getBooksFromCategory(Categories.ALL)
                        books.refresh()
                    },
                    onParkClick = {
                        // получаем список с иыентификатором и
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Park.titleId
                        viewModel.getBooksFromCategory(Categories.PARK)
                        books.refresh()
                    },
                    onSunnyClick = {
                        // получаем список с иыентификатором и
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Sunny.titleId
                        viewModel.getBooksFromCategory(Categories.SUNNY)
                        books.refresh()
                    },
                    onFoodClick = {
                        // получаем список с иыентификатором и
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Food.titleId
                        viewModel.getBooksFromCategory(Categories.FOOD)
                        books.refresh()
                    },
                    onHealthClick = {
                        // получаем список с иыентификатором и
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Health.titleId
                        viewModel.getBooksFromCategory(Categories.HEALTH)
                        books.refresh()
                    }
                )
            }
        ) { paddingValues ->

            if (books.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.empty_list),
                        color = Color.LightGray
                    )
                }
            }
            MyDialog(
                showDialog = showDeleteDialog.value,
                onDismiss = {
                    showDeleteDialog.value = false
                },
                title = "Внимание!",
                massage = "Вы действительно хотите удалить это сообщение?",
                onConfirm = {
                    showDeleteDialog.value = false
                    viewModel.deleteBook(books.itemSnapshotList.items)
                }
            )
            /*  if (books.loadState.refresh is LoadState.Loading) {
                  Box(
                      modifier = Modifier.fillMaxSize(),
                      contentAlignment = Alignment.Center
                  ) {
                      CircularProgressIndicator(
                          modifier = Modifier.size(30.dp)
                      )
                  }
              }*/
            PullToRefreshBox(
                isRefreshing = books.loadState.refresh is LoadState.Loading,
                onRefresh = {
                    books.refresh()
                },
                modifier = Modifier.padding(paddingValues)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(
                        if (viewModel.showTabOneOrTo.value == true) {
                            2
                        } else {
                            1
                        }
                    ),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(count = books.itemCount) { index ->
                        val book = books[index]
                        if (book != null) {
                            BookListItemUi(
                                heightValue = if (viewModel.showTabOneOrTo.value == false) {
                                    230
                                } else {
                                    130
                                },
                                titleIndex = viewModel.categoryState.intValue,
                                isAdminState.value,
                                book,
                                onBookClick = { bk ->
                                    onBookClick(bk)
                                },
                                onEditClick = {
                                    onBookEditClick(it)
                                },
                                onDeleteClick = { bookToDelete ->
                                    showDeleteDialog.value = true
                                    viewModel.bookToDelete = bookToDelete
                                },
                                onFavClick = {
                                    viewModel.onFavesClick(
                                        book,
                                        viewModel.selectedBottomItemState.intValue,
                                        books.itemSnapshotList.items
                                    )

                                }
                            )
                        }
                    }
                }
            }

            FilterDialog(
                showDialog = showFilterDialog,
                onConfirm = {
                    showFilterDialog = false
                    //books.refresh()
                },
                onDismiss = {
                    showFilterDialog = false
                }
            )
        }
    }
}






