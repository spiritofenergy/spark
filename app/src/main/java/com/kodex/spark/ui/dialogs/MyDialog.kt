package com.kodex.spark.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kodex.spark.R
import com.kodex.spark.ui.theme.ButtonColorDark

@Composable
fun MyDialog(
    showDialog: Boolean,
    onDismiss: ()-> Unit,
    onConfirm: ()-> Unit,
    title: String = "Reset Password",
    massage: String,
    confirmButtonText: String = stringResource(R.string.yes)
) {
    if (showDialog){
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton ={
                Button(modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColorDark),
                    onClick = {
                    onConfirm()
                }) {
                    Text(text = confirmButtonText)
                }
                Button(modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColorDark),
                    onClick = {
                    onDismiss()
                }) {
                    Text(text = stringResource(R.string.cansel))
                }
            },
            title= {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = massage,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PrevMyDialog(){
    MyDialog(
        showDialog = true,
        onDismiss = {},
        onConfirm = {},
        massage = "Reset password link was sent to your email. Please check your email and follow the instructions to reset your password."
    )


}