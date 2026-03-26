package com.kodex.spark.ui.commentsScreen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodex.spark.R
import com.kodex.spark.ui.data.NavRoutes
import com.kodex.spark.ui.detailScreen.data.RatingData
import com.kodex.spark.ui.detailScreen.ui.CommentListItem
import com.kodex.spark.ui.detailScreen.ui.DetailsScreenViewModel
import com.kodex.spark.ui.theme.Orange

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@Composable
fun CommentsScreen(
     navObject: NavRoutes.CommentsNavData = NavRoutes.CommentsNavData(),
     viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    var ratingDataToShow by remember { mutableStateOf(RatingData()) }
    LaunchedEffect(key1 = Unit) {
        viewModel.getBookComments(navObject.bookId)
    }
    // Information
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 45.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(
                text = navObject.title,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.width(10.dp))

            Row() {


                if (navObject.ratingsList.isNotEmpty()) {
                    Log.d("MyLog", "DetailScreen ratingsList: ${navObject.ratingsList}")
                    Text(
                        text = String.format("%.1f", navObject.ratingsList.average()),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text =  "(${navObject.ratingsList.size})",
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    )
                } else {
                    Text(text = "Нет оценок")
                }

                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Orange
                )
            }
            }
        Spacer(Modifier.height(10.dp))

        //Comments
        if (viewModel.commentState.value.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            LazyColumn(modifier = Modifier
                .fillMaxSize()) {
                items(viewModel.commentState.value) { ratingData ->
                    CommentListItem(onClick = {
                    },
                        ratingData = ratingData
                    )
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.width(5.dp))
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun CommentsScreenPreview() {
    CommentsScreen( )
}



