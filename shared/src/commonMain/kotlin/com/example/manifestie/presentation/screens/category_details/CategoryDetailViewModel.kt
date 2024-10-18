package com.example.manifestie.presentation.screens.category_details

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
    private val firestoreCategoryDetailRepositoryImpl: FirestoreCategoryDetailRepositoryImpl
): ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(CategoryDetailState())
    val state = _state.asStateFlow()


    fun getQuotesByCategoryId(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        quotes = emptyList()
                    )
                }

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