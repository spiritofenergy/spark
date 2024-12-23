package com.kodex.spark.ui.addScreen

import android.R.attr.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kodex.spark.ui.theme.ButtonColor

 @Composable
fun RoundedCornerDropDownMenu(
     defCategory: String,
     onOptionSelected: (String)-> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(defCategory) }
    val categoriesList = listOf(
        "Корпуса",
        "Питание",
        "Отдых",
        "Развлечения",
        "Бронирование"
    )
    Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .clickable{
                    expanded.value = true
                }
                .padding(20.dp)
            /*    .border(
                    width = 2.dp,
                    color = ButtonColor,
                    shape = RoundedCornerShape(20.dp)*/


            ){
        Text(text = selectedOption.value)

        DropdownMenu(expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }){
            categoriesList.forEach{ option ->
                DropdownMenuItem(
                    text = {
                    Text(text = option)
                }, onClick = {
                    selectedOption.value = option
                    expanded.value = false
                    onOptionSelected(option)
                })
            }
        }
    }
}