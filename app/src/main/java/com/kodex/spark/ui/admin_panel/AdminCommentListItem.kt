package com.kodex.spark.ui.admin_panel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.spark.ui.custom.StarsIndicator
import com.kodex.spark.ui.detailScreen.data.RatingData
import com.kodex.spark.ui.detailScreen.ui.RateDialog
import com.kodex.spark.ui.logon.LoginButton
import com.kodex.spark.ui.theme.DarkWhite
import com.kodex.spark.ui.theme.Orange
import com.kodex.spark.ui.theme.PurpleGrey80
import com.kodex.spark.ui.utils.toFormattedDate
@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminCommentListItem(
    ratingData: RatingData = RatingData(
        name = "Maric",
        rating = 4,
        message = "Very good!"
    ),
    onClickDecline: (RatingData) -> Unit = {},
    onClickAccept: (RatingData) -> Unit = {}

    ) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = DarkWhite
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            StarsIndicator(rating = ratingData.rating)

            Text(
                text = ratingData.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ratingData.message,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ratingData.timestamp.toFormattedDate(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis

            )
             Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                LoginButton(
                    modifier = Modifier.fillMaxWidth().weight(1F)
                        .padding(5.dp),
                    text = "Decline") {
                        onClickDecline(ratingData)
                }
                LoginButton(
                    modifier = Modifier.fillMaxWidth().weight(1F)
                        .padding(5.dp),
                     text = "Accept") {
                        onClickAccept(ratingData)
                }
            }
        }
    }
}
