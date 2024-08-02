package com.example.manifestie.presentation

import com.example.manifestie.core.onError
import com.example.manifestie.core.onSuccess
import com.example.manifestie.data.repository.UnsplashRepositoryImpl
import com.example.manifestie.data.repository.ZenQuotesRepositoryImpl
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomQuoteViewModel(
    private val zenQuotesRepository: ZenQuotesRepositoryImpl,
    private val unsplashRepository: UnsplashRepositoryImpl,
): ViewModel() {

    private val _state = MutableStateFlow(RandomQuoteState())
    val state = _state.asStateFlow()

    suspend fun getRandomPhoto() {
        CoroutineScope(Dispatchers.IO).launch {
            unsplashRepository.getRandomPhoto()
                .onSuccess {
                    _state.update { rState ->
                        rState.copy(imageUrl = it ?: "no data provided" )
                    }
                }
                .onError {
                    _state.update { rState ->
                        rState.copy(
                            error = it
                        )
                    }
                }
        }
    }

    suspend fun getRandomQuote() {
        CoroutineScope(Dispatchers.IO).launch {
            Napier.d(tag = "coroutine scope", message = "inside")
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            zenQuotesRepository.getRandomQuote()
                .onSuccess {
                    _state.update { rState ->
                        rState.copy(quote = it ?: "no data provided" )
                    }
                    Napier.d(tag = "onSuccess", message = it ?: "empty success")
                }
                .onError {
                    _state.update { rState ->
                        rState.copy(
                            error = it
                        )
                    }

                    Napier.d(tag = "onError", message = it.toString())
                }

            _state.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    /*suspend fun flowGetRandomQuote() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _state.update { it.copy(isLoading = true) }

                repository.getRandomQuote()
                    .onSuccess {
                        _state.update { rState ->
                            rState.copy(quote = it ?: "no data provided", isLoading = false)
                        }
                        Napier.d(tag = "onSuccess", message = it ?: "empty success")
                    }
                    .onError {
                        _state.update { rState ->
                            rState.copy(
                                error = it,
                                isLoading = false
                            )
                        }

                        Napier.d(tag = "onError", message = it.toString())
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = NetworkError.UNKNOWN) }
            }
        }
    }*/
}