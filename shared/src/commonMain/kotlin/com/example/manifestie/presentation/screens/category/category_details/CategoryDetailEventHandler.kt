package com.example.manifestie.presentation.screens.category.category_details

import com.example.manifestie.data.repository.FirestoreCategorySharedRepositoryImpl
import com.example.manifestie.domain.model.Quote
import com.example.manifestie.domain.validation.QuoteValidation
import com.example.manifestie.presentation.screens.category.AddQuoteEvent
import com.example.manifestie.presentation.screens.category.CategoryDetailEvent
import com.example.manifestie.presentation.screens.category.CategorySharedViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoryDetailEventHandler(
    private val viewModel: CategorySharedViewModel,
    private val firestoreCategorySharedRepositoryImpl: FirestoreCategorySharedRepositoryImpl
) {
    fun handleSelectCategory(event: CategoryDetailEvent.SelectCategory) {
        viewModel.updateSharedState { it.copy(
            selectedCategoryForQuotes = event.category
        ) }
    }

    fun handleSelectQuote(event: CategoryDetailEvent.SelectQuote) {
        viewModel.updateSharedState { it.copy(
            selectedQuote = event.quote
        ) }
    }

    fun handleOnAddQuoteClick() {
        viewModel.updateAddQuoteState { it.copy(
            sheetOpen = true
        ) }

        Napier.d(tag = "OnAddQuoteClick", message = viewModel.addQuoteState.value.toString())
    }

    fun handleOnQuoteContentChanged(event: AddQuoteEvent.OnQuoteContentChanged) {
        viewModel.updateAddQuoteState { it.copy(
            quote = event.quote
        ) }

        Napier.d(tag = "OnQuoteContentChanged", message = viewModel.addQuoteState.value.toString())
    }

    fun handleOnQuoteSheetDismiss() {
        viewModel.viewModelScope.launch {
            viewModel.updateAddQuoteState {
                it.copy(
                    sheetOpen = false,
                    quoteError = null
                )
            }
            delay(300L) // Animation delay
        }
    }

    fun handleDeleteQuoteFromCategory() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.state.value.selectedCategoryForQuotes?.let { category ->
                viewModel.state.value.selectedQuote?.let { quote ->
                    viewModel.deleteQuoteFromCategory(
                        quote = quote,
                        categoryId = category.id
                    )
                }
            }
            delay(300L)
            viewModel.updateSharedState { it.copy(
                selectedCategoryForQuotes = null,
                selectedQuote = null
            ) }

            Napier.d(tag = "DeleteQuoteFromCategory", message = viewModel.state.value.toString())

        }
    }

    fun handleSaveQuote() {
        submitQuote()
    }

    private fun submitQuote() {
        val result = QuoteValidation.validateQuoteContent(viewModel.addQuoteState.value.quote)

        if (hasValidationErrors(result)) {
            handleValidationErrors(result)
        } else {
            handleSuccessfulSubmission()
        }
    }

    private fun hasValidationErrors(result: QuoteValidation.ValidationResult): Boolean {
        return listOfNotNull(
            result.quoteContentError
        ).isNotEmpty()
    }

    private fun handleValidationErrors(result: QuoteValidation.ValidationResult) {
        viewModel.updateAddQuoteState { it.copy(
            quoteError = result.quoteContentError,
            sheetOpen = true
        ) }
    }

    private fun handleSuccessfulSubmission() {
        if (viewModel.state.value.selectedQuote == null) {
            handleAddQuote()
        } else {
            handleUpdateQuote()
        }
        resetState()
    }

    private fun handleAddQuote() {
        viewModel.state.value.selectedCategoryForQuotes?.let {
            viewModel.addQuote(
                quote = Quote(quote = viewModel.addQuoteState.value.quote),
                categoryId = it.id
            )
        }
    }

    private fun handleUpdateQuote() {
        viewModel.state.value.selectedQuote?.let {
            viewModel.updateQuote(
                quote = Quote(
                    id = it.id,
                    quote = it.quote
                ),
                categoryId = viewModel.state.value.selectedCategoryForQuotes!!.id
            )
        }
    }

    private fun resetState() {
        viewModel.updateAddQuoteState { it.copy(
            quote = "",
            quoteError = null,
            sheetOpen = false
        ) }
        viewModel.updateSharedState { it.copy(
            selectedQuote = null
        ) }
    }

}