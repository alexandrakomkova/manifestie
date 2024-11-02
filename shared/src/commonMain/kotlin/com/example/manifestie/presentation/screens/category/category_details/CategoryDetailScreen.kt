package com.example.manifestie.presentation.screens.category.category_details

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manifestie.presentation.screens.components.ErrorBox
import com.example.manifestie.domain.model.Quote
import com.example.manifestie.presentation.screens.category.AddCategoryEvent
import com.example.manifestie.presentation.screens.category.AddQuoteEvent
import com.example.manifestie.presentation.screens.category.AddQuoteSheetState
import com.example.manifestie.presentation.screens.category.CategoryDetailEvent
import com.example.manifestie.presentation.screens.category.CategorySharedState
import com.example.manifestie.presentation.screens.category.CategorySharedViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch


@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    onUpClick: () -> Unit,
    viewModel: CategorySharedViewModel,
    addQuoteSheetState: AddQuoteSheetState,
    onEvent: (CategoryDetailEvent) -> Unit
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
                    visible = true,
                    enter =  scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    CategoryDetailScreen(
                        state = state,
                        onEvent = { event -> onEvent(event)},
                        addQuoteSheetState = addQuoteSheetState
                    )
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    state: CategorySharedState,
    addQuoteSheetState: AddQuoteSheetState,
    onEvent: (CategoryDetailEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            if (state.quotes.isEmpty()) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "There is no quotes yet :<",
                        color = Color.Black,
                        fontSize = 22.sp)
                }

            } else {
                QuotesList(
                    modifier = Modifier.weight(1f),
                    state = state,
                    onEvent = { event -> onEvent(event)}
                )
            }
            Button(
                onClick = {
                    onEvent(AddQuoteEvent.OnAddQuoteClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(55.dp),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = Color.White,
                    disabledContentColor = Color.LightGray,
                    disabledContainerColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(15.dp),
            ) {
                Text(
                    text = "Create affirmation",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }

    if(addQuoteSheetState.sheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                onEvent(AddQuoteEvent.OnQuoteSheetDismiss)
            },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp,
            dragHandle = {
                Box(
                    modifier = modifier
                        .padding(8.dp)
                        .width(50.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        ) {
            AddQuoteSheet(
                modifier = modifier,
                addQuoteState = addQuoteSheetState,
                state = state,
                onEvent = { event -> onEvent(event) }
            )

        }
    }
}

@Composable
fun QuotesList(
    modifier: Modifier = Modifier,
    state: CategorySharedState,
    onEvent: (CategoryDetailEvent) -> Unit
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
                quote = quote,
                onEvent = { event -> onEvent(event)}
            )
        }
    }

}

@Composable
fun QuoteCard(
    modifier: Modifier = Modifier,
    quote: Quote,
    onEvent: (CategoryDetailEvent) -> Unit
) {
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
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                var expanded by remember { mutableStateOf(false) }

                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                Napier.d(tag = "dropdown edit", message = "edit")
                                onEvent(CategoryDetailEvent.SelectQuote(quote))
                                onEvent(AddQuoteEvent.OnQuoteContentChanged(quote.quote))
                                onEvent(AddQuoteEvent.OnAddQuoteClick)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                onEvent(CategoryDetailEvent.SelectQuote(quote))
                                onEvent(CategoryDetailEvent.DeleteQuoteFromCategory)
                            }
                        )
                    }
                }

                Box(
                    modifier = modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = quote.quote, // "Life is too short to stay angry. Today I'm choosing to be happy and free.",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }


            }
        }
    }
}