package com.kodex.spark.ui.custom

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun MyDialog(
    showDialog: Boolean,
    onDismiss: ()-> Unit,
    onConfirm: ()-> Unit,
    title: String = "Reset Password",
    massage: String,
    confirmButtonText: String = "Да"
) {
    if (showDialog){
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton ={
                Button(onClick = {
                    onConfirm()
                }) {
                    Text(text = confirmButtonText)
                }
                Button(onClick = {
                    onDismiss()
                }) {
                    Text(text = "Cansel")
                }
            },
            title= {
                Text(
                    text = title,
                    color = Color.Gray,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = massage,
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
        )
    }
}
