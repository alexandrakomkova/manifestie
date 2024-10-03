package com.example.manifestie.presentation.screens.category_list.add_category

data class AddCategoryDialogState(
    val title: String = "",
    val titleError: String? = null,
    val dialogOpen: Boolean = false
)