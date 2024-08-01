package com.example.manifestie.presentation

import com.example.manifestie.core.NetworkError

data class RandomQuoteState (
    val isLoading: Boolean = false,
    val quote: String = "",
    val error: NetworkError? = null
)