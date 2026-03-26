package com.kodex.spark.ui.placeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceScreen(
    navController: NavController,
    placeId: String? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Состояния
    var isFavorite by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    //val pagerState = rememberPagerState()

    // Моковые данные (в реальном приложении загружайте из ViewModel)
    val place = remember {
        PlaceData(
            id = "1",
            title = "Кофейня «Уютный уголок»",
            description = "Уютная кофейня в центре города. Мы предлагаем свежую выпечку, авторский кофе и приятную атмосферу для работы и встреч с друзьями. У нас есть бесплатный Wi-Fi, розетки и вежливый персонал.",
            categoryIndex = 0,
            price = "₽₽",
            rating = 4.8f,
            address = "ул. Центральная, 15, Москва",
            isOpen = true,
            workTime = "09:00 - 22:00",
            contact = "info@cozyplace.ru",
            telephone = "+7 (999) 123-45-67",
            site = "https://cozyplace.ru",
            images = listOf(
                "https://picsum.photos/id/20/800/600",
                "https://picsum.photos/id/30/800/600",
                "https://picsum.photos/id/40/800/600",
                "https://picsum.photos/id/50/800/600"
            ),
            isFavorite = false
        )
    }

    // Моковые отзывы
    val reviews = remember {
        listOf(
            Review(
                id = "1",
                userName = "Анна Смирнова",
                userAvatar = null,
                rating = 5.0f,
                text = "Отличное место! Очень вкусный кофе и приятная атмосфера. Обязательно вернусь сюда снова!",
                date = "2 дня назад",
                likes = 12
            ),
            Review(
                id = "2",
                userName = "Михаил Петров",
                userAvatar = null,
                rating = 4.5f,
                text = "Хорошее место для работы. Быстрый Wi-Fi, много розеток. Кофе вкусный, но цены немного высоковаты.",
                date = "5 дней назад",
                likes = 8
            ),
            Review(
                id = "3",
                userName = "Елена Иванова",
                userAvatar = null,
                rating = 5.0f,
                text = "Лучшая кофейня в районе! Обслуживание на высоте, десерты просто великолепны!",
                date = "1 неделю назад",
                likes = 24
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали места", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    // Поделиться
                    IconButton(onClick = { /* Поделиться */ }) {
                        Icon(Icons.Outlined.Share, contentDescription = "Поделиться")
                    }
                    // Избранное
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                            contentDescription = "Избранное",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 1. Галерея изображений
            item {
                ImageGallery(images = place.images)
            }

            // 2. Заголовок и рейтинг
            item {
                TitleAndRatingSection(
                    title = place.title,
                    rating = place.rating,
                    price = place.price
                )
            }

            // 3. Информация о месте
            item {
                InfoSection(
                    address = place.address,
                    isOpen = place.isOpen,
                    workTime = place.workTime,
                    contact = place.contact,
                    telephone = place.telephone,
                    site = place.site
                )
            }

            // 4. Описание
            item {
                DescriptionSection(description = place.description)
            }

            // 5. Кнопка звонка
            item {
                CallButton(telephone = place.telephone)
            }

            // 6. Вкладки с отзывами
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Табы
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Tab(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            text = { Text("Отзывы (${reviews.size})") }
                        )
                        Tab(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            text = { Text("Информация") }
                        )
                    }

                    // Контент вкладок
                    when (selectedTabIndex) {
                        0 -> ReviewsSection(reviews = reviews)
                        1 -> AdditionalInfoSection()
                    }
                }
            }

            // 7. Кнопка "Написать отзыв"
            item {
                WriteReviewButton()
            }
        }
    }
}


@Composable
@Preview(showBackground = true, device = "id:pixel_6")
fun ShowPlaceScreen() {
    PlaceScreen(
        navController = NavController(LocalContext.current),
        placeId = "1"
    )
}