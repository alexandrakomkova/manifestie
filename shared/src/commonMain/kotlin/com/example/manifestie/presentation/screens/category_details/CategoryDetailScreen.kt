package com.example.manifestie.presentation.screens.category_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manifestie.core.ErrorBox
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    onUpClick: () -> Unit,
    viewModel: CategoryDetailViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getQuotesByCategoryId()
        Napier.d(tag = "CategoryDetailScreen - LaunchedEffect", message = state.toString())
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                Napier.d(tag = "CategoryDetailScreen", message = "loading")
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.error != null -> {
                Napier.d(tag = "CategoryDetailScreen", message = "error")

                ErrorBox (
                    errorDescription = state.error.toString(),
                    onTryAgainClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getQuotesByCategoryId()
                        }
                    }
                )
            }
            else -> {
                AnimatedVisibility(
                    visible = state.quotes.isEmpty(),
                    enter =  scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "There is no quotes yet :<",
                        color = Color.Black,
                        fontSize = 22.sp)
                }
                AnimatedVisibility(
                    visible = state.quotes.isNotEmpty(),
                    enter =  scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    QuotesList(
                        state = state
                    )
                }
            }

        }
    }

}

@Composable
fun QuotesList(
    modifier: Modifier = Modifier,
    state: CategoryDetailState
) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalItemSpacing = 10.dp,
    ) {
        items(state.quotes) { quote ->
            QuoteCard(

            )
        }
    }

}

@Composable
fun QuoteCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {


        val largeRadialGradient = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val biggerDimension = maxOf(size.height, size.width)
                return RadialGradientShader(
                    colors =  listOf(
                        Color(0xfff3bbca),
                        Color(0xffe8768f)
                    ),
                    center = size.center,
                    radius = biggerDimension / 2f,
                    colorStops = listOf(0f, 0.95f)
                )
            }
        }


        Box(
            modifier = modifier
                .height(200.dp)
                .background(largeRadialGradient),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Life is too short to stay angry. Today I'm choosing to be happy and free.",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 20.sp
                )

            }
        }
    }
}