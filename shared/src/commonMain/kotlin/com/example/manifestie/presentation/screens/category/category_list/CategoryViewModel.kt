package com.example.manifestie.presentation.screens.category.category_list

import com.example.manifestie.domain.model.Category

interface CategoryViewModel {
    fun addCategory(category: Category)
    fun updateCategory(category: Category)
    fun deleteCategory(category: Category)
}