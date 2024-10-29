package com.example.manifestie.presentation.screens.category

import com.example.manifestie.core.NetworkError
import com.example.manifestie.data.repository.FirestoreCategorySharedRepositoryImpl
import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.model.Quote
import com.example.manifestie.presentation.screens.category.category_list.add_category.CategoryValidation
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

class CategorySharedViewModel(
    private val firestoreCategorySharedRepositoryImpl: FirestoreCategorySharedRepositoryImpl
): ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(CategorySharedState())
    val state = _state.asStateFlow()

    private val _addCategoryState = MutableStateFlow(AddCategorySheetState())
    val addCategoryState = _addCategoryState.asStateFlow()

    private val _addQuoteState = MutableStateFlow(AddQuoteSheetState())
    val addQuoteState = _addQuoteState.asStateFlow()

    fun onEvent(event: CategoryDetailEvent) {
        when(event) {
            is CategoryDetailEvent.DeleteQuoteFromCategory -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.selectedCategoryForQuotes?.let { category ->
                        state.value.selectedQuote?.let { quote ->
                            deleteQuoteFromCategory(
                                quote = quote,
                                categoryId = category.id
                            )
                        }
                    }
                    delay(300L)
                    _state.update { it.copy(
                        selectedCategoryForQuotes = null,
                        selectedQuote = null
                    ) }

                    Napier.d(tag = "DeleteQuoteFromCategory", message = state.value.toString())
                }
            }

            is CategoryDetailEvent.SelectCategory -> {
                _state.update { it.copy(
                    selectedCategoryForQuotes = event.category
                ) }
            }
            is CategoryDetailEvent.SelectQuote -> {
                _state.update { it.copy(
                    selectedQuote = event.quote
                ) }
            }

            AddQuoteEvent.OnAddQuoteClick -> {
                _addQuoteState.update { it.copy(
                    sheetOpen = true
                ) }
                Napier.d(tag = "OnAddQuoteClick", message = addQuoteState.value.toString())
            }
            is AddQuoteEvent.OnQuoteContentChanged -> {
                _addQuoteState.update { it.copy(
                    quote = event.quote
                ) }
                Napier.d(tag = "OnQuoteContentChanged", message = addQuoteState.value.toString())
            }
            AddQuoteEvent.OnQuoteSheetDismiss -> {
                viewModelScope.launch {
                    _addQuoteState.update {
                        it.copy(
                            sheetOpen = false,
                            quoteError = null
                        )
                    }
                    delay(300L) // Animation delay
                }
            }
            AddQuoteEvent.SaveQuote -> {
                submitQuote()
            }
        }
    }

    fun onEvent(event: AddCategoryEvent) {
        when (event) {
            AddCategoryEvent.OnAddCategoryClick -> {
                _addCategoryState.update {
                    it.copy(
                        sheetOpen = true
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

            AddCategoryEvent.OnCategorySheetDismiss -> {
                viewModelScope.launch {
                    _addCategoryState.update {
                        it.copy(
                            sheetOpen = false,
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
                            sheetOpen = false,
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
                        sheetOpen = true,
                        titleError = null
                    )
                }
            }
        }
    }

    private fun submitQuote() { }

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
                    sheetOpen = false,
                    selectedCategory = null
                )
            }
        } else {
            _addCategoryState.update {
                it.copy(
                    titleError = result.categoryTitleError,
                    sheetOpen = true
                )
            }

            Napier.d(tag = "submitData", message = addCategoryState.value.toString())
        }
    }

    private fun addCategory(category: Category) {
        viewModelScope.launch {
            try {
                firestoreCategorySharedRepositoryImpl.addCategory(category = category)
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

                firestoreCategorySharedRepositoryImpl.getCategories().flowOn(Dispatchers.IO)
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
                firestoreCategorySharedRepositoryImpl.updateCategory(category)
            } catch (e: Exception) {
                Napier.d(tag = "onError updateCategory", message = e.message.toString())

            }
        }
    }

    private fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreCategorySharedRepositoryImpl.deleteCategory(category)
                Napier.d(tag = "deleteCategory", message = category.toString())
            } catch (e: Exception) {
                Napier.d(tag = "onError deleteCategory", message = e.message.toString())

            }
        }
    }

    fun getQuotesByCategoryId() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        quotes = emptyList()
                    )
                }

                when(state.value.selectedCategoryForQuotes?.id) {
                    null, "" -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                quotes = emptyList()
                            )
                        }
                    }

                    else -> {
                        firestoreCategorySharedRepositoryImpl.getQuotesByCategoryId(state.value.selectedCategoryForQuotes!!.id)
                            .flowOn(Dispatchers.IO)
                            .collect { result ->
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        error = null,
                                        quotes = result
                                    )
                                }
                            }
                    }
                }

                Napier.d(tag = "getQuotesByCategoryId", message = state.value.toString())
            } catch (e: Exception) {
                Napier.d(tag = "onError getQuotesByCategoryId", message = e.message.toString())
                _state.update {
                    it.copy(
                        error = NetworkError.NO_INTERNET.errorDescription,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateSelectedCategory(category: Category) {
        _state.update {
            it.copy(
                selectedCategoryForQuotes = category
            )
        }
        Napier.d(tag = "updateSelectedCategory", message = state.value.toString())
    }

    fun deleteQuoteFromCategory(quote: Quote, categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreCategorySharedRepositoryImpl.deleteQuoteFromCategory(quote, categoryId)
                Napier.d(tag = "deleteQuoteFromCategory", message = "$quote DELETED FROM $categoryId")
            } catch (e: Exception) {
                Napier.d(tag = "onError deleteQuoteFromCategory", message = e.message.toString())

            }
        }
    }

}