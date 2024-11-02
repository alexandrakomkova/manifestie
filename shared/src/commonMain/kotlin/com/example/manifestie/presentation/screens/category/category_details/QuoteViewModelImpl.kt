package com.example.manifestie.presentation.screens.category.category_details

import com.example.manifestie.data.repository.FirestoreCategorySharedRepositoryImpl
import com.example.manifestie.domain.model.Quote
import com.example.manifestie.presentation.screens.category.CategorySharedViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch


class QuoteViewModelImpl(
    private val viewModel: CategorySharedViewModel,
    private val firestoreCategorySharedRepositoryImpl: FirestoreCategorySharedRepositoryImpl
): QuoteViewModel {
    override fun addQuoteToCategory(quote: Quote, categoryId: String) {
        viewModel.viewModelScope.launch {
            try {
                firestoreCategorySharedRepositoryImpl.addQuoteToCategory(quote = quote, categoryId = categoryId)
                Napier.d(tag = "addQuote", message = "$quote ADDED TO $categoryId")
            } catch (e: Exception) {
                Napier.d(tag = "onError addQuote", message = e.message.toString())
            }
        }
    }

    override fun updateQuoteFromCategory(quote: Quote, categoryId: String) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreCategorySharedRepositoryImpl.updateQuoteFromCategory(quote, categoryId)
            } catch (e: Exception) {
                Napier.d(tag = "onError updateQuote", message = e.message.toString())

            }
        }
    }

    override fun deleteQuoteFromCategory(quote: Quote, categoryId: String) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreCategorySharedRepositoryImpl.deleteQuoteFromCategory(quote, categoryId)
                Napier.d(tag = "deleteQuoteFromCategory", message = "$quote DELETED FROM $categoryId")
            } catch (e: Exception) {
                Napier.d(tag = "onError deleteQuoteFromCategory", message = e.message.toString())

            }
        }
    }

}