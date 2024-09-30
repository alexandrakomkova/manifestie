package com.example.manifestie.presentation.screens.random_quote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.manifestie.core.ErrorBox
import com.example.manifestie.presentation.screens.quote_card.QuoteBigCard
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
                QuoteBigCard(modifier, state)
            }
        }
    }
}