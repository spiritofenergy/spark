import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.bottom_menu.BottomMenu
import com.kodex.spark.ui.bottom_menu.BottomMenuItem
import com.kodex.spark.ui.data.MainScreenDataObject
import com.kodex.spark.ui.drawer_menu.DrawerBody
import com.kodex.spark.ui.drawer_menu.DrawerHeader
import com.kodex.spark.ui.mainScreen.BookListItemUi
import com.kodex.spark.ui.mainScreen.MSViewModel
import com.kodex.spark.R
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodex.spark.ui.custom.MyDialog
import com.kodex.spark.ui.top_app_bar.MainTopBar


@SuppressLint("SuspiciousIndentation")
@Composable
fun MenuScreen(
    viewModel: MSViewModel = hiltViewModel(),
    navData: MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onBookClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
) {
    val context = LocalContext.current
    val showLoadIndicator = remember {
        mutableStateOf(false)
    }
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember {
        mutableStateOf(false)
    }
    val isAdminState = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        if (viewModel.booksListState.value.isEmpty()) {
            viewModel.getAllBooks()
        }
        viewModel.uiState.collect { state ->
            when (state) {
                is MSViewModel.MainUiState.Loading -> {
                    showLoadIndicator.value = true
                }

                is MSViewModel.MainUiState.Success -> {
                    showLoadIndicator.value = false
                }

                is MSViewModel.MainUiState.Error -> {
                    showLoadIndicator.value = false
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
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
                    onFavesClick = {
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Faves.titleId
                        viewModel.getAllFavesBooks()
                        coroutineScope.launch {
                            drawerState.close()
                        }

                    },
                    onAdminClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onAdminClick()
                    },
                    onCategoryClick = { categoryIndex ->
                        viewModel.getBooksFromCategory(categoryIndex)
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Faves.titleId
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },
                    onAllClick = {
                        viewModel.getAllBooks()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                MainTopBar(viewModel.categoryState.intValue)
            },
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    viewModel.selectedBottomItemState.value,
                    onFavesClick = {
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Faves.titleId
                        viewModel.getAllFavesBooks()
                    },
                    onHomeClick = {
                        // получаем список с иыентификатором и
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Home.titleId
                        viewModel.getAllBooks()
                    }
                )
            }
        ) { paddingValues ->

            if (viewModel.isFavesListEmptyState.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
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
                    viewModel.deleteBook()
                }
            )
            if (showLoadIndicator.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(100.dp)
                    )
                }


            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                items(viewModel.booksListState.value) { book ->
                    BookListItemUi(
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
                            viewModel.onFavClick(book, viewModel.selectedBottomItemState.value)
                        }
                    )
                }
            }
        }
    }
}






