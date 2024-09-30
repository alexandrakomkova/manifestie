package com.example.manifestie.presentation.screens.category_list

import com.example.manifestie.domain.model.Category

data class CategoryListState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String = ""
)