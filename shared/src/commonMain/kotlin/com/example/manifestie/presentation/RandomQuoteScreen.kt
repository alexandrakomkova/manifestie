package com.example.manifestie.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import com.example.manifestie.core.ErrorBox
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

@Composable
fun RandomQuoteScreen(
    modifier: Modifier = Modifier,
    viewModel: RandomQuoteViewModel = getKoin().get(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRandomQuote()
        viewModel.getRandomPhoto()
        Napier.d(tag = "LaunchedEffect", message = state.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when {
            state.isLoading -> {
                Napier.d(tag = "RandomQuoteScreen", message = "loading")
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            state.error != null -> {
                Napier.d(tag = "RandomQuoteScreen", message = "error")

                ErrorBox (
                    errorDescription = state.error.toString(),
                    onTryAgainClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getRandomQuote()
                            viewModel.getRandomPhoto()
                        }
                    }
                )
            }
            state.imageUrl.isNotBlank() || state.quote.isNotBlank() -> {
                Napier.d(tag = "RandomQuoteScreen", message = "show")
                RandomQuoteScreen(modifier, state)
            }
        }
    }
}

@Composable
fun RandomQuoteScreen(
    modifier: Modifier = Modifier,
    state: RandomQuoteState
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        //contentAlignment = Alignment.Center
    ) {
        var sizeImage by remember { mutableStateOf(IntSize.Zero) }

        SubcomposeAsyncImage(
            model = state.imageUrl,
            contentDescription = "cocktail_img"
        ) {
            val painterState = painter.state
            if (painterState is AsyncImagePainter.State.Loading || painterState is AsyncImagePainter.State.Error) {
                CircularProgressIndicator()
            } else {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { sizeImage = it.size }
                        .clip(MaterialTheme.shapes.medium)
                        .drawWithCache {
                            val gradient = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = size.height / 3,
                                endY = size.height
                            )
                            onDrawWithContent {
                                drawContent()
                                drawRect(gradient, blendMode = BlendMode.Multiply)
                            }
                        }
                )
            }
        }

        Text(
            text = state.quote,
            //"When you know what you want, and want it bad enough, you will find a way to get it.",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp)
                .align(Alignment.BottomCenter)
        )


    }
}