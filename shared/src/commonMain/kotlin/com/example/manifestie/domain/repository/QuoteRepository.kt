package com.example.manifestie.domain.repository

import com.example.manifestie.domain.model.Quote
import kotlinx.coroutines.flow.Flow


interface QuoteRepository {
    fun getQuotesByCategoryId(categoryId: String): Flow<List<Quote>>

    suspend fun addQuoteToCategory(quote: Quote, categoryId: String)
    suspend fun  updateQuoteFromCategory(quote: Quote, categoryId: String)
    suspend fun deleteQuoteFromCategory(quote: Quote, categoryId: String)
}