package com.kodex.spark.ui.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.spark.R
import com.kodex.spark.ui.custom.StarsIndicator
import com.kodex.spark.ui.detailScreen.data.RatingData
import com.kodex.spark.ui.theme.ButtonColorDark
import com.kodex.spark.ui.utils.toFormattedDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
    fun DialogComments(
    showDialog: Boolean,
    onDismiss: ()-> Unit,
    onConfirm: () -> Unit,
    ratingData: RatingData = RatingData(),
    confirmButtonText: String = stringResource(R.string.close )
    ) {
        if (showDialog){
            AlertDialog(
                onDismissRequest = {
                    onDismiss()
                },
                confirmButton ={
                    Button(onClick = {
                        onConfirm()
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColorDark
                    )) {
                        Text(text = confirmButtonText)
                    }
                },
                title = {
                    Column(Modifier.fillMaxWidth()) {
                        StarsIndicator(rating = ratingData.rating)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = ratingData.name,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                },
                text = {
                    Column() {
                        Text(
                            text = ratingData.message,
                            color = Color.Black,
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
                    }

                }
            )
        }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun PrevDialogComments() {
    DialogComments(
        ratingData = RatingData(
            name = "my_adress@mail.ru",
            rating = 5,
            message = "Good very book!"
        ),
        onDismiss = {},
        onConfirm = {},
        showDialog = true,
        confirmButtonText = "yes"
    )
}

/*showDialog: Boolean,
    onDismiss: ()-> Unit,
    ratingData: RatingData = RatingData(),
    confirmButtonText: String = stringResource(R.string.yes )*/