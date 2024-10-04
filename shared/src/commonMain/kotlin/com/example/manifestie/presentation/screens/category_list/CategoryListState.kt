package com.example.manifestie.presentation.screens.category_list

import com.example.manifestie.domain.model.Category

data class CategoryListState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

sealed interface AddCategoryEvent {
    data object OnAddCategoryClick: AddCategoryEvent
    data class OnCategoryTitleChanged(val title: String): AddCategoryEvent
    data object OnCategoryDialogDismiss: AddCategoryEvent

    data object SaveCategory: AddCategoryEvent
}

data class AddCategoryState (
    val title: String = "",
    val titleError: String? = null,
    val dialogOpen: Boolean = false
)

