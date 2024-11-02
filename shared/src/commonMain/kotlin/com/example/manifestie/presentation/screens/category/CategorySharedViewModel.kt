package com.example.manifestie.presentation.screens.category

import com.example.manifestie.core.NetworkError
import com.example.manifestie.data.repository.FirestoreCategorySharedRepositoryImpl
import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.model.Quote
import com.example.manifestie.presentation.screens.category.category_details.CategoryDetailEventHandler
import com.example.manifestie.presentation.screens.category.category_list.CategoryEventHandler
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class CategorySharedViewModel(
    private val firestoreCategorySharedRepositoryImpl: FirestoreCategorySharedRepositoryImpl
): ViewModel(), KoinComponent {

    private val _categorySharedState = MutableStateFlow(CategorySharedState())
    val state = _categorySharedState.asStateFlow()

    private val _addCategoryState = MutableStateFlow(AddCategorySheetState())
    val addCategoryState = _addCategoryState.asStateFlow()

    private val _addQuoteState = MutableStateFlow(AddQuoteSheetState())
    val addQuoteState = _addQuoteState.asStateFlow()

    private val categoryEventHandlers = CategoryEventHandler(this, firestoreCategorySharedRepositoryImpl)
    private val categoryDetailEventHandlers = CategoryDetailEventHandler(this, firestoreCategorySharedRepositoryImpl)

    fun updateSharedState(update: (CategorySharedState) -> CategorySharedState) {
        _categorySharedState.update { update(it) }
    }

    fun updateAddCategoryState(update: (AddCategorySheetState) -> AddCategorySheetState) {
        _addCategoryState.update { update(it) }
    }

    fun updateAddQuoteState(update: (AddQuoteSheetState) -> AddQuoteSheetState) {
        _addQuoteState.update { update(it) }
    }

    fun onEvent(event: CategoryDetailEvent) {
        when(event) {
            is CategoryDetailEvent.DeleteQuoteFromCategory -> categoryDetailEventHandlers.handleDeleteQuoteFromCategory()
            is CategoryDetailEvent.SelectCategory -> categoryDetailEventHandlers.handleSelectCategory(event)
            is CategoryDetailEvent.SelectQuote -> categoryDetailEventHandlers.handleSelectQuote(event)
            AddQuoteEvent.OnAddQuoteClick -> categoryDetailEventHandlers.handleOnAddQuoteClick()
            is AddQuoteEvent.OnQuoteContentChanged -> categoryDetailEventHandlers.handleOnQuoteContentChanged(event)
            AddQuoteEvent.OnQuoteSheetDismiss -> categoryDetailEventHandlers.handleOnQuoteSheetDismiss()
            AddQuoteEvent.SaveQuote -> categoryDetailEventHandlers.handleSaveQuote()
        }
    }

    fun onEvent(event: AddCategoryEvent) {
        when (event) {
            AddCategoryEvent.OnAddCategoryClick -> categoryEventHandlers.handleOnAddCategoryClick()
            is AddCategoryEvent.OnCategoryTitleChanged -> categoryEventHandlers.handleOnCategoryTitleChanged(event)
            AddCategoryEvent.SaveCategory -> categoryEventHandlers.handleSaveCategory()
            AddCategoryEvent.OnCategorySheetDismiss -> categoryEventHandlers.handleOnCategorySheetDismiss()
            AddCategoryEvent.DeleteCategory -> categoryEventHandlers.handleDeleteCategory()
            is AddCategoryEvent.SelectCategory -> categoryEventHandlers.handleSelectCategory(event)
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                _categorySharedState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        categories = emptyList()
                    )
                }

                firestoreCategorySharedRepositoryImpl.getCategories().flowOn(Dispatchers.IO)
                    .collect { result ->
                        _categorySharedState.update { categoryListState ->
                            categoryListState.copy(
                                isLoading = false,
                                error = null,
                                categories = result
                            )
                        }
                    }

            } catch (e: Exception) {
                Napier.d(tag = "onError getCategoryFromFirestore", message = e.message.toString())
                _categorySharedState.update { categoryListState ->
                    categoryListState.copy(
                        error = NetworkError.NO_INTERNET.errorDescription,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateSelectedCategoryForQuotes(category: Category) {
        _categorySharedState.update {
            it.copy(
                selectedCategoryForQuotes = category
            )
        }
        Napier.d(tag = "updateSelectedCategory", message = state.value.toString())
    }

    fun getQuotesByCategoryId() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _categorySharedState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        quotes = emptyList()
                    )
                }

                when(state.value.selectedCategoryForQuotes?.id) {
                    null, "" -> {
                        _categorySharedState.update {
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
                                _categorySharedState.update {
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
                _categorySharedState.update {
                    it.copy(
                        error = NetworkError.NO_INTERNET.errorDescription,
                        isLoading = false
                    )
                }
            }
        }
    }

}