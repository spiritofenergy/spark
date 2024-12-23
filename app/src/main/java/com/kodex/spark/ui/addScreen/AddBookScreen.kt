package com.kodex.spark.ui.addScreen

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.spark.R
import com.kodex.spark.ui.logon.LoginButton
import com.kodex.spark.ui.logon.RoundedCornerTextField
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.kodex.spark.ui.addScreen.data.AddScreenObject
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.theme.BoxFilter


@Composable
fun AddBookScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: ()-> Unit ={}
) {
    val cv = LocalContext.current.contentResolver

    var selectedCategory = remember {
        mutableStateOf(navData.category)
    }
    val navImageUrl = remember {
        mutableStateOf(navData.imageUrl)
    }
    val description = remember {
        mutableStateOf(navData.description)
    }
    val prise = remember {
        mutableStateOf(navData.prise)
    }

    val title = remember {
        mutableStateOf(navData.title)
    }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val imageBitMap = remember {
        var bitmap: Bitmap? = null
        try {
            val base64Image = Base64.decode(navData.imageUrl, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(
                base64Image, 0,
                base64Image.size
            )
        }catch (e: IllegalArgumentException){
        }
        mutableStateOf(bitmap)
    }
    val firestore = remember {
        Firebase.firestore
    }
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageBitMap.value = null
        selectedImageUri.value = uri
    }

    //фон
    Image(painter = painterResource(id = R.drawable.way),
        contentDescription = "Logo",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop

    )
    Box(modifier = Modifier.fillMaxSize()
        .background(BoxFilter)
    )

       // Основной лист
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(46.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                model = imageBitMap.value ?: selectedImageUri.value),
            contentDescription = "",
            modifier = Modifier.height(200.dp).padding(bottom = 50.dp)

        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "ИСКРА",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(40.dp))

if (selectedCategory.value.isEmpty()){
    selectedCategory.value = "Выберете категорию"
}
        RoundedCornerDropDownMenu (selectedCategory.value){ selectedItem ->
            imageLauncher
            selectedCategory.value = selectedItem
        }
        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = title.value,
            label = "Название:"
        ) {
            title.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = description.value,
            label = "Краткое описание:",
            singleLine = false,
            maxLines = 5
        ) {
            description.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = prise.value,
            label = "Цена:"
        ) {
            prise.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

/*
        if (prise.value.isNotEmpty()){
            Text(
                text = prise.value,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }*/

        LoginButton(text = "Выбрать фото") {
            imageLauncher.launch("image/*")
        }
        LoginButton(text = "Сохранить ") {
            saveBookToFireStore(
                firestore,
                Book(
                key = navData.key,
                title = title.value,
                description = description.value,
                prise = prise.value,
                category = selectedCategory.value,
                isFaves = navData.isFaves,
                imageUrl = if (selectedImageUri.value != null)
                 imageToBase64(
                     selectedImageUri.value!!,
                     cv
                 )   else navData.imageUrl
            ),
                    onSaved = {
                        onSaved()
                    },
                    onError = {
                  }
             )
        }
    }
}

fun imageToBase64(uri: Uri, contentResolver: ContentResolver): String{
    val inputStream = contentResolver.openInputStream(uri)
    val bytes = inputStream?.readBytes()
    return bytes?.let {
        Base64.encodeToString(it, Base64.DEFAULT)
    } ?: ""
}

private fun saveBookToFireStore(
    firestore: FirebaseFirestore,
    book: Book,
    onSaved: ()-> Unit,
    onError: ()-> Unit
){
    val db = firestore.collection("spark_posts")
    //val key = if(book.key.isEmpty()) db.document().id else book.key
    val key = book.key.ifEmpty{ db.document().id}
    db.document(key)
        .set(
            book.copy(key = key)
        ).addOnSuccessListener{
            onSaved()
        }
        .addOnFailureListener{
            onError()
    }
}

