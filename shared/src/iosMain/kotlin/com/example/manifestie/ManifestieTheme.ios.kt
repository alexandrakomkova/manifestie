package com.example.manifestie

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun ManifestieTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}