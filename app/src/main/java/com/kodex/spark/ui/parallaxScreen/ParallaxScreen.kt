package com.kodex.spark.ui.parallaxScreen
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kodex.spark.ui.data.NavRoutes
import com.kodex.spark.ui.detailScreen.data.RatingData
import com.kodex.spark.ui.detailScreen.ui.DetailsScreenViewModel
import kotlinx.coroutines.launch

// Модель данных места


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxScreen(
   // onCommentClick: (NavRoutes.CommentsNavData) -> Unit = {},
   // navObject: NavRoutes.ParallaxScreenObject = NavRoutes.ParallaxScreenObject(),
    viewModel: DetailsScreenViewModel = hiltViewModel(),


    navObject: NavRoutes.ParallaxScreenObject = NavRoutes.ParallaxScreenObject(),
    onBackPressed: () -> Unit,
    onCallTaxi: (String, String) -> Unit,
    onNavigateToReviews: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val collapsedHeight = 280.dp
    val expandedHeight = screenHeight * 0.5f

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val imageHeight = remember { Animatable(expandedHeight.value) }

    var bitmap: Bitmap? = null
    try {
        val base64Image = Base64.decode(navObject.imageUrl, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(
            base64Image, 0,
            base64Image.size
        )
    } catch (e: IllegalArgumentException) {

    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getBookComments(navObject.bookId)
    }

    // NestedScroll для Parallax эффекта
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newHeight = (imageHeight.value - delta).coerceIn(
                    collapsedHeight.value,
                    expandedHeight.value
                )
                coroutineScope.launch {
                    imageHeight.animateTo(
                        newHeight,
                        animationSpec = tween(durationMillis = 0)
                    )
                }
                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = navObject.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Поделиться */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Поделиться")
                    }
                    IconButton(onClick = { /* Избранное */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Избранное")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Фото-галерея с Parallax эффектом
                item {
                    AsyncImage(
                        model = bitmap,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 20.dp)
                            .height(250.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray),
                        contentScale = ContentScale.FillHeight
                    )
                }

                // Основной контент
                item {
                    MainContent(
                        navObject = navObject,
                        //onCallTaxi = { onCallTaxi(navObject.latitude, navObject.longitude) },
                        onNavigateToReviews = onNavigateToReviews
                    )
                }
            }
        }
    }
}


// Для демонстрации добавьте в Preview:
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun PreviewDetailScreen() {
    MaterialTheme {
        ParallaxScreen(
            navObject = NavRoutes.ParallaxScreenObject(
                bookId = "1",
                title = "Coffee House & Bakery",
                address = "ул. Тверская, 15, Москва",


                price = 25,
                imageUrl = "" ,
                isOpenNow = true,
                openingHours = "09:00 - 23:00",
                telephone = "+7 (495) 123-45-67",
                website = "coffeehouse.ru",
                latitude = "55",
                longitude = "37"
            ),
            onBackPressed = {},
            onCallTaxi = { _, _ -> },
            onNavigateToReviews = {}
        )
    }
}