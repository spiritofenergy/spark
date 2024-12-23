import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.addScreen.data.Favorite
import com.kodex.spark.ui.bottom_menu.BottomMenu
import com.kodex.spark.ui.bottom_menu.BottomMenuItem
import com.kodex.spark.ui.data.MainScreenDataObject
import com.kodex.spark.ui.drawer_menu.DrawerBody
import com.kodex.spark.ui.drawer_menu.DrawerHeader
import com.kodex.spark.ui.mainScreen.BookListItemUi
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun MenuScreen(
    navData: MainScreenDataObject,
    onBookEditClick: (Book)-> Unit,
    onBookClick: (Book)-> Unit,
    onAdminClick: ()-> Unit

){
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val coroutineScope = rememberCoroutineScope()
    val bookListState = remember {
        mutableStateOf(emptyList <Book>())
    }
    val selectedBottomItemState = remember {
        mutableStateOf(BottomMenuItem.Home.title)
    }
    val isAdminState = remember {
        mutableStateOf(false)
    }
    val isFavesListEmptyState = remember {
        mutableStateOf(false)
    }
    val categoryState = remember {
        mutableStateOf("Fantasy")
    }
    val db = remember {
        Firebase.firestore
    }
    LaunchedEffect(Unit) {
        getAllFavesIds(db, navData.uid) {faves ->
            getAllBooks(db, faves) { books ->
                isFavesListEmptyState.value = books.isEmpty()
                bookListState.value = books
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
                        selectedBottomItemState.value = BottomMenuItem.Faves.title
                            getAllFavesIds(db, navData.uid) { faves ->
                                getAllFavesBooks(db, faves) { books ->
                                    isFavesListEmptyState.value = books.isEmpty()
                                    bookListState.value = books
                                }
                            }
                            coroutineScope.launch {
                                drawerState.close()
                            }

                        },
                    onAdminClick = {
                        coroutineScope.launch{
                            drawerState.close()
                        }
                        onAdminClick()
                    },
                    onCategoryClick = { category ->
                        getAllFavesIds(db, navData.uid) { faves ->
                            if (category =="All"){
                                getAllBooks(db,faves){ books ->
                                    bookListState.value = books
                                }
                            }else
                                getAllBooksFromCategory(db, faves, category) { books ->
                                    bookListState.value = books
                                }
                        }
                        coroutineScope.launch{
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {


        Scaffold (
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    selectedBottomItemState.value,
                    onFavesClick = {
                        selectedBottomItemState.value = BottomMenuItem.Faves.title

                        getAllFavesIds(db, navData.uid) {faves ->
                            getAllFavesBooks(db, faves) { books ->
                                isFavesListEmptyState.value = books.isEmpty()
                                bookListState.value = books
                            }
                        }
                    },
                    onHomeClick = {
                        // получаем список с иыентификатором и
                        selectedBottomItemState.value = BottomMenuItem.Home.title
                        getAllFavesIds(db, navData.uid) {faves ->
                            getAllBooks(db, faves) { books ->
                                isFavesListEmptyState.value = books.isEmpty()
                                bookListState.value = books
                            }
                        }
                    }
                )
            }
        ){ paddingValues ->

            if (isFavesListEmptyState.value){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Лист пустой",
                        color = Color.LightGray
                    )
                }
            }

            LazyVerticalGrid(columns = GridCells.Fixed(1),
            modifier = Modifier.fillMaxWidth()
                .padding(paddingValues)
            ) {
                items(bookListState.value){book ->
                    BookListItemUi(
                        isAdminState.value,
                        book,
                        onBookClick = {bk->
                            onBookClick(bk)
                        },
                        onEditClick = {
                        onBookEditClick(it)
                        },
                        onFavClick = {
                            bookListState.value = bookListState.value.map { bk ->
                                if (bk.key == book.key){
                                    onFaves(
                                        db,
                                        navData.uid,
                                        Favorite(bk.key),
                                        !bk.isFaves
                                    )
                                    bk.copy(isFaves = !bk.isFaves)
                                }else{
                                    bk
                                }
                            }
                            if (selectedBottomItemState.value == BottomMenuItem.Faves.title)
                            bookListState.value = bookListState.value.filter {it.isFaves

                            }
                        }
                    )
                }
            }
        }
    }
}

private fun getAllBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
     onBooks: (List<Book>)-> Unit
){
    db.collection("spark_posts")

        .get()
        .addOnSuccessListener{ task ->
            val bookList = task.toObjects(Book::class.java).map {
                if (idsList.contains(it.key)){
                    it.copy(isFaves = true)
                }else{
                    it
                }
            }
            onBooks(bookList)
        }
        .addOnFailureListener{

        }
}


private fun getAllFavesBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
    onBooks: (List<Book>)-> Unit
){// проверяем, что список не пустрой
    if(idsList.isNotEmpty()) {
        db.collection("spark_posts")
            .whereIn(FieldPath.documentId(), idsList)
            .get()
            .addOnSuccessListener { task ->
                val bookList = task.toObjects(Book::class.java).map {
                    if (idsList.contains(it.key)) {
                        it.copy(isFaves = true)
                    } else {
                        it
                    }
                }
                onBooks(bookList)
            }
            .addOnFailureListener {

            }
    }else{
        onBooks(emptyList())
    }
}

private fun getAllBooksFromCategory(
    db: FirebaseFirestore,
    idsList: List<String>,
    category: String,
    onBooks: (List<Book>)-> Unit
){
    db.collection("spark_posts")
        .whereEqualTo("category", category)
        .get()
        .addOnSuccessListener{task ->
            val bookList = task.toObjects(Book::class.java).map {
                if (idsList.contains(it.key)){
                    it.copy(isFaves = true)
                }else{
                    it
                }
            }
            onBooks(bookList)
        }
        .addOnFailureListener{

        }
}
private fun getAllFavesIds(
    db: FirebaseFirestore,
    uid: String,
    onFaves:(List<String>)-> Unit
){
    db.collection("spark_users")
        .document(uid)
        .collection("spark_faves")
        .get()
        .addOnSuccessListener{ task ->
            val idsList = task.toObjects(Favorite::class.java)
            val keysList = arrayListOf<String>()
            idsList.forEach{
                keysList.add(it. key)
            }
            onFaves(keysList)
        }
        .addOnFailureListener{

        }
}

private fun onFaves(
    db: FirebaseFirestore,
    uid: String,
    favorite: Favorite,
    isFav: Boolean,
){
    if(isFav){
        db.collection("spark_users")
            .document(uid)
            .collection("spark_faves")
            .document(favorite.key)
            .set(favorite)
    }else{
        db.collection("spark_users")
            .document(uid)
            .collection("spark_faves")
            .document(favorite.key)
            .delete()
    }
}