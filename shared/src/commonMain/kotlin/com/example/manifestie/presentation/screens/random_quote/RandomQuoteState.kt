package com.example.manifestie.presentation.screens.random_quote

import com.example.manifestie.domain.model.Category

data class RandomQuoteState (
    val isLoading: Boolean = false,
    val quote: String = "",
    val imageUrl: String = "",
    val error: String? = null
)

sealed interface RandomQuoteEvent {
    data object OnLikeQuoteClick: RandomQuoteEvent
    data object OnChooseCategorySheetDismiss: RandomQuoteEvent
    data class SelectCategory(val category: Category): RandomQuoteEvent

    data object SaveQuote: RandomQuoteEvent
}

data class ChooseCategoryState(
    val sheetOpen: Boolean = false,
    val selectedCategory: Category? = null
)