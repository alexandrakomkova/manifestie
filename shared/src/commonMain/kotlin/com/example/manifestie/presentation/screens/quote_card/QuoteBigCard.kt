package com.example.manifestie.presentation.screens.quote_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.manifestie.presentation.screens.random_quote.RandomQuoteEvent
import com.example.manifestie.presentation.screens.random_quote.RandomQuoteState

@Composable
fun QuoteBigCard(
    modifier: Modifier = Modifier,
    state: RandomQuoteState,
    onEvent: (RandomQuoteEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
    ) {
        var sizeImage by remember { mutableStateOf(IntSize.Zero) }

        SubcomposeAsyncImage(
            model = state.imageUrl,
            contentDescription = "quote_background_img"
        ) {
            val painterState = painter.state
            if (painterState is AsyncImagePainter.State.Loading || painterState is AsyncImagePainter.State.Error) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { sizeImage = it.size }
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

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(50.dp)
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.quote, // "When you know what you want, and want it bad enough, you will find a way to get it.",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val checked = remember { mutableStateOf(false) }

                IconButton(
                    modifier = modifier.size(48.dp),
                    onClick = { onEvent(RandomQuoteEvent.OnLikeQuoteClick) }
                ) {
                    Icon(
                        if (checked.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "favourite_btn",
                        tint = if (checked.value) Color.Red else Color.White,
                    )
                }
            }
        }
    }
}