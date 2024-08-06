package com.example.manifestie.presentation

data class RandomQuoteState (
    val isLoading: Boolean = false,
    val quote: String = "",
    val imageUrl: String = "",
    val error: String? = null
)