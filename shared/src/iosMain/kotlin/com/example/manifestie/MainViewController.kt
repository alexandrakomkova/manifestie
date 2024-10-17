package com.example.manifestie

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    App(
        darkTheme = false,
        dynamicColor = false
    )
}