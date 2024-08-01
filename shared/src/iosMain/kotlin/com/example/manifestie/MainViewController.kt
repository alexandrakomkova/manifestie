package com.example.manifestie

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.example.manifestie.data.network.ZenQuotesClient
import com.example.manifestie.data.network.createHttpClient
import io.ktor.client.engine.darwin.Darwin

fun MainViewController() = ComposeUIViewController {
    App()
}