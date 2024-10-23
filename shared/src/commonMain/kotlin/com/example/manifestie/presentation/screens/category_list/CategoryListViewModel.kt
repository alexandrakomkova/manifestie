package com.example.manifestie.presentation.screens.category_list

import com.example.manifestie.core.NetworkError
import com.example.manifestie.data.repository.FirestoreCategoryRepositoryImpl
import com.example.manifestie.domain.model.Category
import com.example.manifestie.presentation.screens.category_details.CategoryDetailState
import com.example.manifestie.presentation.screens.category_list.add_category.CategoryValidation
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class CategoryListViewModel(
    private val firestoreCategoryRepositoryImpl: FirestoreCategoryRepositoryImpl
): ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(CategoryListState())
    val state = _state.asStateFlow()

    private val _addCategoryState = MutableStateFlow(AddCategoryState())
    val addCategoryState = _addCategoryState.asStateFlow()

    private val categoryDetailState = MutableStateFlow(CategoryDetailState())

    fun onEvent(event: AddCategoryEvent) {
        when (event) {
            AddCategoryEvent.OnAddCategoryClick -> {
                _addCategoryState.update {
                    it.copy(
                        dialogOpen = true
                    )
                }
                Napier.d(tag = "OnAddCategoryClick", message = addCategoryState.value.toString())

            }

            is AddCategoryEvent.OnCategoryTitleChanged -> {
                _addCategoryState.update {
                    it.copy(
                        title = event.title
                    )
                }
                Napier.d(
                    tag = "OnCategoryTitleChanged",
                    message = addCategoryState.value.toString()
                )

            }

            AddCategoryEvent.SaveCategory -> {
                submitData()
            }

            AddCategoryEvent.OnCategoryDialogDismiss -> {
                viewModelScope.launch {
                    _addCategoryState.update {
                        it.copy(
                            dialogOpen = false,
                            titleError = null
                        )
                    }
                    delay(300L) // Animation delay
                }
            }

            AddCategoryEvent.DeleteCategory -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addCategoryState.value.selectedCategory?.let {
                        deleteCategory(it)
                    }

                    delay(300L)
                    _addCategoryState.update {
                        it.copy(
                            selectedCategory = null,
                            dialogOpen = false,
                            title = "",
                            titleError = null
                        )
                    }

                    Napier.d(tag = "DeleteCategory", message = addCategoryState.value.toString())
                    Napier.d(tag = "DeleteCategory", message = state.value.toString())
                }
            }

            is AddCategoryEvent.SelectCategory -> {
                _addCategoryState.update {
                    it.copy(
                        selectedCategory = event.category,
                        title = event.category.title,
                        dialogOpen = true,
                        titleError = null
                    )
                }
            }
        }
    }

    private fun submitData() {
        val result = CategoryValidation.validateCategoryTitle(addCategoryState.value.title)
        val hasError = listOfNotNull(
            result.categoryTitleError
        )

        if (hasError.isEmpty()) {

            if (addCategoryState.value.selectedCategory == null) {
                addCategory(
                    Category(title = addCategoryState.value.title)
                )
            } else {
                addCategoryState.value.selectedCategory?.let { category ->
                    updateCategory(
                        Category(
                            id = category.id,
                            title = addCategoryState.value.title
                        )
                    )
                }
            }

            _addCategoryState.update {
                it.copy(
                    title = "",
                    titleError = null,
                    dialogOpen = false,
                    selectedCategory = null
                )
            }
        } else {
            _addCategoryState.update {
                it.copy(
                    titleError = result.categoryTitleError,
                    dialogOpen = true
                )
            }

            Napier.d(tag = "submitData", message = addCategoryState.value.toString())
        }
    }

    private fun addCategory(category: Category) {
        viewModelScope.launch {
            try {
                firestoreCategoryRepositoryImpl.addCategory(category = category)
                Napier.d(tag = "addCategory", message = category.toString())
            } catch (e: Exception) {
                Napier.d(tag = "onError addCategory", message = e.message.toString())
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        categories = emptyList()
                    )
                }

                firestoreCategoryRepositoryImpl.getCategories().flowOn(Dispatchers.IO)
                    .collect { result ->
                        _state.update { categoryListState ->
                            categoryListState.copy(
                                isLoading = false,
                                error = null,
                                categories = result
                            )
                        }
                    }

            } catch (e: Exception) {
                Napier.d(tag = "onError getCategoryFromFirestore", message = e.message.toString())
                _state.update { categoryListState ->
                    categoryListState.copy(
                        error = NetworkError.NO_INTERNET.errorDescription,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreCategoryRepositoryImpl.updateCategory(category)
            } catch (e: Exception) {
                Napier.d(tag = "onError updateCategory", message = e.message.toString())

            }
        }
    }

    private fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreCategoryRepositoryImpl.deleteCategory(category)
                Napier.d(tag = "deleteCategory", message = category.toString())
            } catch (e: Exception) {
                Napier.d(tag = "onError deleteCategory", message = e.message.toString())

            }
        }
    }
}

