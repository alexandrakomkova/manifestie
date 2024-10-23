package com.example.manifestie.domain.repository

import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface CategorySharedRepository {
    fun getCategories(): Flow<List<Category>>
    fun getCategoryById(id: String): Flow<Category>
    suspend fun addCategory(category: Category)
    suspend fun  updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)

    fun getQuotesByCategoryId(categoryId: String): Flow<List<Quote>>
}