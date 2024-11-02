package com.example.manifestie.presentation.screens.category

import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.model.Quote

data class CategorySharedState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val quotes: List<Quote> = emptyList(),
    val error: String? = null,
    val selectedCategoryForQuotes: Category? = null,
    val selectedQuote: Quote? = null
)

sealed interface CategoryListEvent

sealed interface AddCategoryEvent: CategoryListEvent {
    data object OnAddCategoryClick: AddCategoryEvent
    data class OnCategoryTitleChanged(val title: String): AddCategoryEvent
    data object OnCategorySheetDismiss: AddCategoryEvent

    data object DeleteCategory : AddCategoryEvent
    data class SelectCategory(val category: Category): AddCategoryEvent

    data object SaveCategory: AddCategoryEvent
}

data class AddCategorySheetState (
    val title: String = "",
    val titleError: String? = null,
    val sheetOpen: Boolean = false,
    val selectedCategory: Category? = null
)

sealed interface CategoryDetailEvent {
    data object DeleteQuoteFromCategory: CategoryDetailEvent
    data class SelectCategory(val category: Category): CategoryDetailEvent
    data class SelectQuote(val quote: Quote): CategoryDetailEvent
}

data class AddQuoteSheetState (
    val quote: String = "",
    val quoteError: String? = null,
    val sheetOpen: Boolean = false,
)

sealed interface AddQuoteEvent: CategoryDetailEvent {
    data object OnAddQuoteClick: AddQuoteEvent
    data class OnQuoteContentChanged(val quote: String): AddQuoteEvent
    data object OnQuoteSheetDismiss: AddQuoteEvent

    data object SaveQuote: AddQuoteEvent
}