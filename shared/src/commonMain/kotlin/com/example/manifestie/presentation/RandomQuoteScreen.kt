package com.example.manifestie.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun RandomQuoteScreen(
    modifier: Modifier = Modifier,
    viewModel: Int = 0
) {
    RandomQuoteScreen(modifier)
}

@Composable
fun RandomQuoteScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "I'm going to have a good day",
            fontSize = 24.sp,
            color = Color.Black
        )
    }
}