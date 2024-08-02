package com.example.manifestie

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.manifestie.di.appModules
import com.example.manifestie.presentation.RandomQuoteScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.compose.KoinApplication


@Composable
fun App() {
    KoinApplication(application = {
        modules(appModules())
    }) {
        ManifestieTheme {

            Napier.base(DebugAntilog())

            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                RandomQuoteScreen()
            }
        }
    }

}
