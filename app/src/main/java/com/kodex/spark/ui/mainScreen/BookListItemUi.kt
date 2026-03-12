package com.kodex.spark.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kodex.spark.R
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.theme.Orange
import com.kodex.spark.ui.utils.toBitmap
import kotlin.Int

@Composable
fun BookListItemUi(
    heightValue: (Int) = 0,
    titleIndex: Int = 0,
    showEditButton: Boolean = false,
    book: Book = Book(),
    onEditClick: (Book) -> Unit = {},
    onFavesClick: () -> Unit = {},
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            // 1. Фоновое изображение
            AsyncImage(
                model = book.imageUrl.toBitmap(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(10.dp))
            )

            // 2. Категория в левом верхнем углу
            Text(
                " " + stringArrayResource(R.array.category_arrays)[book.categoryIndex] + " ",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 10.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            )

            // 3. MoreVert в правом верхнем углу
            IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "",
                    modifier = Modifier
                        .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
                        .padding(8.dp),
                    tint = Color.White
                )
            }

            // 4. Bookmark в правом нижнем углу
            IconButton(
                onClick = { onFavesClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    if (book.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "",
                    modifier = Modifier
                        .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
                        .padding(8.dp),
                    tint = if (book.isFavorite) colorResource(R.color.orang) else Color.White
                )
            }

            // 5. Рейтинг в левом нижнем углу
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .padding(horizontal = 10.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (book.ratingsList.isNotEmpty()) {
                    Text(
                        text = String.format("%.1f", book.ratingsList.average()),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                } else {
                    Text(text = "0.0")
                }
                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Orange
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = book.title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 10.dp)
        )

      /*  Text(
            text = stringArrayResource(id = R.array.category_arrays)[book.categoryIndex],
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 10.dp)

        )*/
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


           /* IconButton(onClick = {
                onFavesClick()
            }) {
                Icon(
                    if (book.isFavorite) {
                        Icons.Default.Favorite

                    } else
                        Icons.Default.FavoriteBorder,
                    contentDescription = "",
                    tint = if (book.isFavorite) Color.Red else Color.Gray
                )
            }*/


            //IconButton { }(painter = painterResource(id = R.drawable))
        }

    }
}
@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    BookListItemUi(
        book = Book(

        )
    )
}
