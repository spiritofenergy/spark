package com.kodex.spark.ui.admin_panel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kodex.spark.ui.theme.PurpleGrey80


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ModerationScreen() {
    Box(Modifier.fillMaxSize().background(PurpleGrey80)){
        LazyColumn (modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)){
            items(10){
                AdminCommentListItem()
                Spacer(modifier = Modifier.height(5.dp))

            }
        }
    }
}