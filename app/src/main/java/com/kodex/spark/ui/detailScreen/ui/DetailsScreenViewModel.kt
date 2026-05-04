package com.kodex.spark.ui.detailScreen.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.spark.ui.data.NavRoutes
import com.kodex.spark.ui.detailScreen.data.RatingData
import com.kodex.spark.ui.utils.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private var notEmpty: Boolean = TODO("initialize me")

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging
) : ViewModel() {

    // val  ratingState  = mutableStateOf("0")
    val commentState = mutableStateOf(emptyList<RatingData>())
    val ratingDataState = mutableStateOf<RatingData?>(RatingData())
    var isOpen = mutableStateOf(false)


    fun insertRating(ratingData: RatingData, bookId: String) {
        fireStoreManager.insertUserComment(ratingData, bookId)
    }

    fun getBookComments(bookId: String) = viewModelScope.launch {
        commentState.value = fireStoreManager.getBookComments(bookId)
    }

    fun getUserRating(bookId: String) = viewModelScope.launch {
        ratingDataState.value = fireStoreManager.getUserRating(bookId)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkIsOpen(openingHours: String): Boolean {
        val parts = openingHours.split(" - ")
        if (parts.size != 2) return false

        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val openTime = LocalTime.parse(parts[0], formatter)
        val closeTime = LocalTime.parse(parts[1], formatter)
        val now = LocalTime.now()

        // Правильный возврат значения
        return now >= openTime && now < closeTime
    }

    // Функция для обновления статуса открытости
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateOpenStatus(openingHours: String) {
        isOpen.value = checkIsOpen(openingHours)
        Log.d("IsOpenLog"," ${isOpen.value}")
    }
    // Выносим логику шаринга в отдельную функцию
    suspend fun sharePlace(
        context: Context,
        place: NavRoutes.ParallaxNavObject,
        coroutineScope: kotlinx.coroutines.CoroutineScope
    ) {
        withContext(Dispatchers.IO) {

            val shareText = buildString {
                val averageRating = if (place.ratingsList.isNotEmpty()) {
                    place.ratingsList.average()
                } else 0.0

                appendLine(place.title)
                appendLine()
                appendLine("⭐️ Рейтинг: ${averageRating} / ${place.ratingsList.size}")
                appendLine("📍 Адрес: ${place.address}")
                appendLine("📞 Телефон: ${place.telephone}")
                appendLine("🕐 Режим работы: ${place.openingHours}")
                if (place.website.isNotEmpty()) {
                    appendLine("🌐 Сайт: ${place.website}")
                }
                appendLine()
                appendLine(place.description)
                appendLine()
                appendLine("Поделиться из приложения Искра Кучугуры")
            }

            withContext(Dispatchers.Main) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                context.startActivity(
                    Intent.createChooser(shareIntent, "Поделиться местом")
                )
            }
        }





        }

        // Функция для форматирования числа
        fun Double.format(digits: Int) = "%.${digits}f".format(this)

    }
