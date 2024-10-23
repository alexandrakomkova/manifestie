package com.example.manifestie.presentation.screens.category_details

import com.example.manifestie.core.NetworkError
import com.example.manifestie.data.repository.FirestoreCategoryDetailRepositoryImpl
import com.example.manifestie.domain.model.Category
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

class CategoryDetailViewModel(
    private val firestoreCategoryDetailRepositoryImpl: FirestoreCategoryDetailRepositoryImpl,
): ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(CategoryDetailState())
    val state = _state.asStateFlow()

    private val categoryId = state.value.category?.id ?: ""

    init {
        getQuotesByCategoryId()
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

                Napier.d(tag = "categoryId", message = categoryId)
                if(categoryId.isEmpty()) {
                    _state.update { categoryDetailState ->
                        categoryDetailState.copy(
                            isLoading = false,
                            error = null,
                            quotes = emptyList()
                        )
                    }
                    Napier.d(tag = "getQuotesByCategoryId", message = "empty")
                } else {
                    firestoreCategoryDetailRepositoryImpl.getQuotesByCategoryId(categoryId).flowOn(Dispatchers.IO)
                        .collect { result ->
                            _state.update { categoryDetailState ->
                                categoryDetailState.copy(
                                    isLoading = false,
                                    error = null,
                                    quotes = result
                                )
                            }

                            Napier.d(tag = "getQuotesByCategoryId", message = result.toString())
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
                category = category
            )
        }

        Napier.d(tag = "updateSelectedCategory", message = state.value.toString())
    }
}