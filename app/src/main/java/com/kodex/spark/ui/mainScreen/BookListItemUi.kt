package com.kodex.spark.ui.mainScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kodex.spark.R
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.utils.toBitmap

@Composable
fun BookListItemUi(
    heightValue: (Int) = 0,
    titleIndex: Int,
    showEditButton: Boolean = false,
    book: Book = Book(),
    onEditClick: (Book) -> Unit = {},
    onFavClick: () -> Unit = {},
    onBookClick: (Book) -> Unit = {},
    onDeleteClick: (Book) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onBookClick(book)
            }
    ) {
        AsyncImage(
            model = book.imageUrl.toBitmap(),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                //.height(100.dp)
                .height(heightValue.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = book.title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 10.dp)
        )

        Text(
            text = stringArrayResource(id = R.array.category_arrays)[book.categoryIndex],
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 10.dp)

        )
        Text(
            text = book.description,
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 10.dp)
        )



        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1F)) {
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp),
                    text = book.price.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,

                    )
                Text(
                    modifier = Modifier
                        .weight(1F)
                        .padding(1.dp),
                    text = "p",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,

                    )
            }


            if (showEditButton)
                IconButton(
                    onClick = {
                        onEditClick(book)
                    }) {
                    Icon(
                        Icons.Default.Edit,
                        modifier = Modifier
                         .weight(1F)
                         .padding(1.dp),
                        contentDescription = ""
                    )
                }


            if (showEditButton)
                IconButton(onClick = {
                    onDeleteClick(book)
                }) {
                    Icon(
                        Icons.Default.Delete,
                        modifier = Modifier
                         .weight(1F)
                         .padding(1.dp),
                        contentDescription = ""
                    )
                }


            IconButton(onClick = {
                onFavClick()
            }) {
                Icon(
                    if (book.isFaves) {
                        Icons.Default.Favorite

                    } else
                        Icons.Default.FavoriteBorder,
                    contentDescription = "",
                    tint = if (book.isFaves) Color.Red else Color.Gray
                )
            }


            //IconButton { }(painter = painterResource(id = R.drawable))
        }

    }
}
