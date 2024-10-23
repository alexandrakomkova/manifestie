package com.example.manifestie.presentation.screens.category

import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.model.Quote

data class CategorySharedState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val quotes: List<Quote> = emptyList(),
    val error: String? = null,
    val selectedCategoryForQuotes: Category? = null
)

sealed interface AddCategoryEvent {
    data object OnAddCategoryClick: AddCategoryEvent
    data class OnCategoryTitleChanged(val title: String): AddCategoryEvent
    data object OnCategoryDialogDismiss: AddCategoryEvent

    data object DeleteCategory : AddCategoryEvent
    data class SelectCategory(val category: Category): AddCategoryEvent

    data object SaveCategory: AddCategoryEvent
}

data class AddCategoryState (
    val title: String = "",
    val titleError: String? = null,
    val dialogOpen: Boolean = false,
    val selectedCategory: Category? = null
)