package com.example.manifestie.presentation.screens.random_quote

import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomQuoteEventHandler(
    private val viewModel: RandomQuoteViewModel
) {

    fun handleOnLikeQuoteClick() {
        viewModel.viewModelScope.launch {
            viewModel.updateChooseCategoryState { it.copy(
                    sheetOpen = true
                )
            }
            Napier.d(tag = "OnLikeQuoteClick", message = viewModel.chooseCategoryState.value.toString())
        }
    }

    fun handleOnChooseCategorySheetDismiss() {
        viewModel.viewModelScope.launch {
            viewModel.updateChooseCategoryState { it.copy(
                sheetOpen = false
            ) }
            delay(300L)
        }
    }

    fun handleSelectCategory(event: RandomQuoteEvent.SelectCategory) {
        viewModel.updateChooseCategoryState { it.copy(
            selectedCategory = event.category
        ) }
    }
    fun handleSaveQuoteToCategory() {
        submitData()
    }

    private fun submitData() {
        Napier.d(tag = "OnLikeQuoteClick", message = "submitData")

        viewModel.addQuoteToCategory()
        viewModel.viewModelScope.launch {
            viewModel.updateChooseCategoryState { it.copy(
                selectedCategory = null,
                sheetOpen = false
            ) }
            delay(300L)
        }
    }
}