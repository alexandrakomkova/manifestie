package com.example.manifestie.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.onError
import com.example.manifestie.core.onSuccess
import com.example.manifestie.data.network.ZenQuotesClient
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

@Composable
fun RandomQuoteScreen(
    modifier: Modifier = Modifier,
    viewModel: RandomQuoteViewModel = getKoin().get(),
    state: RandomQuoteState = viewModel.state.value,
    client: ZenQuotesClient = getKoin().get()
) {



    LaunchedEffect(Unit) {
        viewModel.getRandomQuote()
        Napier.d(tag = "LaunchedEffect", message = state.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(state.isLoading) {
            Napier.d(tag = "RandomQuoteScreen", message = "loading")
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        if(state.error != null) {
            Napier.d(tag = "RandomQuoteScreen", message = "error")
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 170.dp,
                        bottom = 70.dp
                    ),
                text = state.error.name,
                color = Color.Red,
                textAlign = TextAlign.Center,
            )
        }

        if(state.quote.isNotBlank()) {
            Napier.d(tag = "RandomQuoteScreen", message = "show")
            RandomQuoteScreen(modifier, state.quote)
        }
    }

    Napier.d(tag = "RandomQuoteScreen", message = state.quote)


}

@Composable
fun RandomQuoteScreen(
    modifier: Modifier = Modifier,
    quote: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = quote,
            fontSize = 24.sp,
            color = Color.Black
        )
    }
}