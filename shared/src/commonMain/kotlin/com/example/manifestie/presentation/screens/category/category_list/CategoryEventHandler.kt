package com.example.manifestie.presentation.screens.category.category_list

import com.example.manifestie.data.repository.FirestoreCategorySharedRepositoryImpl
import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.validation.CategoryValidation
import com.example.manifestie.presentation.screens.category.AddCategoryEvent
import com.example.manifestie.presentation.screens.category.CategorySharedViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
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

    fun handleSaveCategory() {
        submitCategory()
    }

    private fun submitCategory() {
        val result = CategoryValidation.validateCategoryTitle(viewModel.addCategoryState.value.title)

        if (hasValidationErrors(result)) {
            handleValidationErrors(result)
        } else {
            handleSuccessfulSubmission()
        }
    }

    private fun hasValidationErrors(result: CategoryValidation.ValidationResult): Boolean {
        return listOfNotNull(
            result.categoryTitleError
        ).isNotEmpty()
    }

    private fun handleValidationErrors(result: CategoryValidation.ValidationResult) {
        viewModel.updateAddCategoryState  {
            it.copy(
                titleError = result.categoryTitleError,
                sheetOpen = true
            )
        }

        Napier.d(tag = "submitData", message = viewModel.addCategoryState.value.toString())
    }

    private fun handleSuccessfulSubmission() {
        if (viewModel.addCategoryState.value.selectedCategory == null) {
           handleAddCategory()
        } else {
            handleUpdateCategory()
        }
        resetState()
    }

    private fun handleAddCategory() {
        viewModel.addCategory(
            Category(title = viewModel.addCategoryState.value.title)
        )
    }

    private fun handleUpdateCategory() {
        viewModel.addCategoryState.value.selectedCategory?.let { category ->
            viewModel.updateCategory(
                Category(
                    id = category.id,
                    title = viewModel.addCategoryState.value.title
                )
            )
        }
    }

    private fun resetState() {
        viewModel.updateAddCategoryState {
            it.copy(
                title = "",
                titleError = null,
                sheetOpen = false,
                selectedCategory = null
            )
        }
    }
}