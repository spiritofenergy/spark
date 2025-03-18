package com.kodex.spark.ui.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.addScreen.data.Favorite
import javax.inject.Singleton

@Singleton
class FirestoreManager(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
        private fun getAllFavesIds(
        onFaves: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        getFavesCategoryReference().get()
            .addOnSuccessListener { task ->
                val idsList = task.toObjects(Favorite::class.java)
                val keysList = arrayListOf<String>()

                idsList.forEach {
                    keysList.add(it.key)
                }
                onFaves(keysList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Ошибка считывания ID")
            }
    }

    fun getAllFavesBooks(
        onBooks: (List<Book>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        getAllFavesIds(
            onFaves = { idsList ->
                    if (idsList.isNotEmpty()) {
                        db.collection("spark_postsw")
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
                            .addOnFailureListener {exception->
                                onFailure(exception.message ?: "Error")
                                Log.d("Error", "Не удалось построить список")
                            }
                    } else {
                        onBooks(emptyList())
                    }
                },
            onFailure = {message->
                onFailure(message)
            }
        )
    }


    fun getAllBooksFromCategory(
        categoryIndex: Int,
        onBooks: (List<Book>) -> Unit,
        onFailure: (String) -> Unit

    ) {
        getAllFavesIds(
            onFaves = {idsList ->
                    db.collection("spark_posts")
                        .whereEqualTo("category", categoryIndex)
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
                        .addOnFailureListener {exeption ->
                            onFailure(exeption.message ?: "Error reading book from category")
                        }
                },
                onFailure = {message ->
                     onFailure(message)

            }
        )
    }

    fun getAllBooks(
        onBooks: (List<Book>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        getAllFavesIds (
            onFaves ={ idsList ->
                db.collection("spark_posts")
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
                    .addOnFailureListener {exception->
                            onFailure(exception.message ?: "Error reading all books"
                        )
                    }
            },
                onFailure = {message->
                    onFailure(message)
            }
        )
    }

    fun changeFavState(books: List<Book>, book: Book): List<Book> {
        return books.map { bk ->
            if (bk.key == book.key) {
                onFaves(
                    Favorite(bk.key),
                    !bk.isFaves
                )
                bk.copy(isFaves = !bk.isFaves)
            } else {
                bk
            }
        }
    }

    fun onFaves(
        favorite: Favorite,
        isFav: Boolean,
    ) {
        val favesDokRef = getFavesCategoryReference()
            .document(favorite.key)
        if (isFav) {
            favesDokRef.set(favorite)
        } else {
            favesDokRef.delete()
        }
    }

    fun deleteBook(book: Book,
                   onDeleted:()-> Unit,
                   onFailure: (String) -> Unit
    ){
        db.collection("spark_posts")
            .document(book.key)
            .delete()
            .addOnSuccessListener{
                    onDeleted()
            }
            .addOnFailureListener{exception ->
                onFailure(exception.message ?: "Error deleting book")

            }
    }
    fun getFavesCategoryReference(): CollectionReference {
        return db.collection("spark_users")
            .document(auth.uid ?: "")
            .collection("spark_faves")
    }
}
