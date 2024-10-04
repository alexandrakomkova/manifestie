package com.example.manifestie

import androidx.compose.runtime.Composable

@Composable
expect fun ManifestieTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
)