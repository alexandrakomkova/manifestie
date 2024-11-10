package com.example.manifestie.presentation.screens.random_quote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.manifestie.presentation.screens.components.ErrorBox
import com.example.manifestie.presentation.screens.quote_card.QuoteBigCard
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomQuoteScreen(
    modifier: Modifier = Modifier,
    viewModel: RandomQuoteViewModel = getKoin().get(),
    chooseCategoryState: ChooseCategoryState,
    onEvent: (RandomQuoteEvent) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.getRandomQuote()
        viewModel.getRandomPhoto()
        Napier.d(tag = "RandomQuoteScreen - LaunchedEffect", message = state.toString())
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
                QuoteBigCard(
                    modifier = modifier,
                    state = state,
                    onEvent = { event -> onEvent(event) }
                )
            }
        }
    }

    if(chooseCategoryState.sheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                onEvent(RandomQuoteEvent.OnChooseCategorySheetDismiss)
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
            ChooseCategorySheet(
                chooseCategoryState = chooseCategoryState,
                onEvent = { event -> onEvent(event)}
            )
        }
    }
}