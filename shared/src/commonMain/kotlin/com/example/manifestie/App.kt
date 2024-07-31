package com.example.manifestie

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.manifestie.presentation.RandomQuoteScreen

@Composable
fun App(modifier: Modifier = Modifier) {
    ManifestieTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            RandomQuoteScreen()
        }
    }
}
