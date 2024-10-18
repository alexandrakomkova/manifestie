package com.example.manifestie.domain.repository

import com.example.manifestie.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface CategoryDetailRepository {
    fun getQuotesByCategoryId(categoryId: String): Flow<List<Quote>>
}