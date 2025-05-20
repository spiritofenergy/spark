package com.kodex.spark.ui.detailScreen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.spark.ui.detailScreen.data.RatingData
import com.kodex.spark.ui.theme.DarkWhite
import com.kodex.spark.ui.theme.Orange
import com.kodex.spark.ui.theme.PurpleGrey80

@Composable
fun CommentListItem(
    ratingData: RatingData
) {
    Card (modifier = Modifier.width(250.dp),
            colors = CardDefaults.cardColors(
            containerColor = DarkWhite
        )
    ){
        Column (modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            ){
            Row (modifier = Modifier.fillMaxWidth()){
                for (i in 1..ratingData.rating) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Orange
                )
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ratingData.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1

                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ratingData.message,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis

                )
        }
    }
}