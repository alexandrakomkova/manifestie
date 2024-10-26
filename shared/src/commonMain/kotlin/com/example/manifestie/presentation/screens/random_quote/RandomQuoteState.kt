package com.example.manifestie.presentation.screens.random_quote

import com.example.manifestie.domain.model.Category
import com.example.manifestie.presentation.screens.category.AddCategoryEvent

data class RandomQuoteState (
    val isLoading: Boolean = false,
    val quote: String = "",
    val imageUrl: String = "",
    val error: String? = null
)

sealed interface RandomQuoteEvent {
    data object OnAddQuoteClick: RandomQuoteEvent
    data object OnAddQuoteSheetDismiss: RandomQuoteEvent


    data object SaveQuote: RandomQuoteEvent
}

data class ChooseCategoryState(
    val sheetOpen: Boolean = false,
    val selectedCategory: List<Category> = emptyList()
)