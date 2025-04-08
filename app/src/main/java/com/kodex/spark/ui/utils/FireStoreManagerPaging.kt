package com.kodex.spark.ui.utils

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.addScreen.data.Favorite
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

const val IS_BASE_64 = true

@Singleton
class FireStoreManagerPaging(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
   // private val contentResolver: FirebaseStorage,
   // private val storage: FirebaseStorage,
) {
    var categoryIndex = Categories.ALL
    var searchText = ""
    // private val isBase64 = false


    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?,
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection("spark_posts").limit(pageSize)
        val keysFavesList = getIdsFavesList()

        query = when (categoryIndex) {
            Categories.ALL -> query
            Categories.FAVORITES -> query.whereEqualTo(FieldPath.documentId(), keysFavesList)
            else -> query.whereEqualTo("categoryIndex", categoryIndex)
        }
        if (searchText.isNotEmpty()){
            query = query.whereGreaterThanOrEqualTo("title", searchText) //"Test"
        }

        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }
        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(Book::class.java)
        val updatedBooks = books.map {
            if (keysFavesList.contains(it.key)) {
                it.copy(isFaves = true)
            } else {
                it
            }
        }
        return Pair(querySnapshot, updatedBooks)
    }


    private suspend fun getIdsFavesList(): List<String> {
        var snapshot = getFavesCategoryReference().get().await()
        val idsList = snapshot.toObjects(Favorite::class.java)
        val keysList = arrayListOf<String>()

        idsList.forEach {
            keysList.add(it.key)
        }
        return if (keysList.isEmpty()) listOf("-1") else keysList
    }

    fun getFavesCategoryReference(): CollectionReference {
        return db.collection("spark_users")
            .document(auth.uid ?: "")
            .collection("spark_faves")
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

    fun deleteBook(
        book: Book,
        onDeleted: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        db.collection("spark_posts")
            .document(book.key)
            .delete()
            .addOnSuccessListener {
                onDeleted()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error deleting book")

            }
    }

    private fun uploadImageToFirestore(
        oldImageUrl: String,
        uri: Uri?,
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val timeStamp = System.currentTimeMillis()
        val storageRef = if (oldImageUrl.isEmpty()) {
           // storage!!.reference
             //   .child("spark_posts")
             //   .child("image_$timeStamp.jpg")
        } else {
           // storage?.getReferenceFromUrl(oldImageUrl)
        }
        if (uri == null) {
            saveBookToFireStore(
                book.copy(imageUrl = oldImageUrl),
                onSaved = {
                    onSaved()
                },
                onError = { massage ->
                    onError(massage)
                }
            )
            return
        }
//        val imageBytes = ImageUtils.uriToBiteArray(uri, contentResolver)
//        val uploadTask = storageRef?.putBytes(imageBytes)
//        uploadTask?.addOnSuccessListener{
//            storageRef.downloadUrl.addOnSuccessListener{url ->
//                saveBookToFireStore(
//                    book.copy(imageUrl = url.toString()),
//                    onSaved = {
//                        onSaved()
//                    },
//                    onError = {massage->
//                        onError(massage)
//                    }
//                )
//            }
//        }
    }

    fun saveBookImage(
        oldImageUrl: String,
        uri: Uri?,
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (IS_BASE_64) {
            saveBookToFireStore(
                book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError("Error save Image1 ")
                },
            )
        } else {
            uploadImageToFirestore(
                oldImageUrl = oldImageUrl,
                uri = uri,
                book = book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError("Error save Image2")
                }
            )
        }
    }

    fun saveBookToFireStore(
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val db = db.collection("spark_posts")
        val key = book.key.ifEmpty { db.document().id }
        db.document(key)
            .set(
                book.copy(key = key)
            ).addOnSuccessListener {
                onSaved()
            }
            .addOnFailureListener {

            }
        onError
    }
}
