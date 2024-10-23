package com.example.manifestie.presentation.screens.category_details

import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.model.Quote

data class CategoryDetailState(
    val isLoading: Boolean = false,
    val quotes: List<Quote> = emptyList(),
    val error: String? = null,
    val category: Category? = null
)