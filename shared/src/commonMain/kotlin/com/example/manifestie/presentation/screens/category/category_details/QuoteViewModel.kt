package com.example.manifestie.presentation.screens.category.category_details

import com.example.manifestie.domain.model.Quote

interface QuoteViewModel {
    fun addQuoteToCategory(quote: Quote, categoryId: String)
    fun  updateQuoteFromCategory(quote: Quote, categoryId: String)
    fun deleteQuoteFromCategory(quote: Quote, categoryId: String)
}