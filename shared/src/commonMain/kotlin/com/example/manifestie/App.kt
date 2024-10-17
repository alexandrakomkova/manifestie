package com.example.manifestie

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.manifestie.presentation.navigation.AppNavigation
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier


@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    ManifestieTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {

        Napier.base(DebugAntilog())

        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
             //RandomQuoteScreen()
            AppNavigation()
        }
    }
}
