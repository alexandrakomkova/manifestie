package com.example.manifestie.presentation.screens.category_details

import androidx.lifecycle.SavedStateHandle
import com.example.manifestie.core.NAV_CATEGORY_DETAIL
import com.example.manifestie.core.NetworkError
import com.example.manifestie.data.repository.FirestoreCategoryDetailRepositoryImpl
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
    val savedStateHandle: SavedStateHandle,
): ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(CategoryDetailState())
    val state = _state.asStateFlow()

    private val categoryId: String = savedStateHandle.get<String>(NAV_CATEGORY_DETAIL) ?: ""


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

                if(categoryId.isEmpty()) {
                    _state.update { categoryDetailState ->
                        categoryDetailState.copy(
                            isLoading = false,
                            error = null,
                            quotes = emptyList()
                        )
                    }
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
                        }
                }

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
}