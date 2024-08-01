package com.example.manifestie

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.manifestie.di.appModules
import com.example.manifestie.presentation.RandomQuoteScreen
import org.koin.compose.KoinApplication


@Composable
fun App() {
    KoinApplication(application = {
        modules(appModules())
    }) {
        ManifestieTheme {

            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                RandomQuoteScreen()
            }
        }
    }

}
