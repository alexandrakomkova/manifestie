package com.example.manifestie

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.manifestie.ui.theme.darkScheme
import com.example.manifestie.ui.theme.lightScheme

@Composable
actual fun ManifestieTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content,
        colorScheme = if(darkTheme) darkScheme else lightScheme,
    )
}