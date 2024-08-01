package com.example.manifestie.presentation

import com.example.manifestie.core.onError
import com.example.manifestie.core.onSuccess
import com.example.manifestie.data.ZenQuotesRepositoryImpl
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomQuoteViewModel(
    private val repository: ZenQuotesRepositoryImpl
): ViewModel() {

    private val _state = MutableStateFlow(RandomQuoteState())
    val state = _state.asStateFlow()

    suspend fun getRandomQuote() {
        viewModelScope.launch(Dispatchers.IO) {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            repository.getRandomQuote()
                .onSuccess {
                    _state.update { rState ->
                        rState.copy(quote = it ?: "no data provided" ) }
                }
                .onError {
                    _state.update { rState ->
                        rState.copy(
                            error = it
                        )
                    }
                }

            _state.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

}