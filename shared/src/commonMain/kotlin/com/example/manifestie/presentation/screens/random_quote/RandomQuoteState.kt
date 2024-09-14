package com.example.manifestie.presentation.screens.random_quote

data class RandomQuoteState (
    val isLoading: Boolean = false,
    val quote: String = "",
    val imageUrl: String = "",
    val error: String? = null
)