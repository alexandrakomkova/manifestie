package com.example.manifestie.presentation.screens.category.category_list

import com.example.manifestie.data.repository.FirestoreCategorySharedRepositoryImpl
import com.example.manifestie.presentation.screens.category.AddCategoryEvent
import com.example.manifestie.presentation.screens.category.CategorySharedViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoryEventHandler(
    private val viewModel: CategorySharedViewModel,
    private val firestoreCategorySharedRepositoryImpl: FirestoreCategorySharedRepositoryImpl
) {
    fun handleOnAddCategoryClick() {
        viewModel.updateAddCategoryState {
            it.copy(
                sheetOpen = true
            )
        }
        Napier.d(tag = "OnAddCategoryClick", message = viewModel.addCategoryState.value.toString())
    }

    fun handleOnCategoryTitleChanged(event: AddCategoryEvent.OnCategoryTitleChanged) {
        viewModel.updateAddCategoryState {
            it.copy(title = event.title)
        }
        Napier.d(
            tag = "OnCategoryTitleChanged",
            message = viewModel.addCategoryState.value.toString()
        )
    }

    fun handleSaveCategory() {
        viewModel.submitCategory()
    }

    fun handleOnCategorySheetDismiss() {
        viewModel.viewModelScope.launch {
            viewModel.updateAddCategoryState {
                it.copy(
                    sheetOpen = false,
                    titleError = null
                )
            }
            delay(300L) // Animation delay
        }
    }

    fun handleDeleteCategory() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.addCategoryState.value.selectedCategory?.let {
                viewModel.deleteCategory(it)
            }
            delay(300L)
            viewModel.updateAddCategoryState {
                it.copy(
                    selectedCategory = null,
                    sheetOpen = false,
                    title = "",
                    titleError = null
                )
            }
            Napier.d(tag = "DeleteCategory", message = viewModel.addCategoryState.value.toString())
            Napier.d(tag = "DeleteCategory", message = viewModel.state.value.toString())
        }
    }

    fun handleSelectCategory(event: AddCategoryEvent.SelectCategory) {
        viewModel.updateAddCategoryState {
            it.copy(
                selectedCategory = event.category,
                title = event.category.title,
                sheetOpen = true,
                titleError = null
            )
        }
    }
}