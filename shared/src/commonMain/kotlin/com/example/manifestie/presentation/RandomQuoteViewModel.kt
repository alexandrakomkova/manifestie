package com.example.manifestie.presentation

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.onError
import com.example.manifestie.core.onSuccess
import com.example.manifestie.data.datastore.DataStoreHelper
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
            try {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        quote = ""
                    )
                }

                unsplashRepository.getRandomPhoto()
                    .onSuccess {
                        _state.update { rState ->
                            rState.copy(
                                isLoading = false,
                                imageUrl = it ?: "no data provided"
                            )
                        }
                        DataStoreHelper.updateQuote(it)
                    }
                    .onError {
                        _state.update { rState ->
                            rState.copy(
                                error = it.errorDescription,
                                isLoading = false
                            )
                        }
                    }
            } catch (e: Exception) {
                Napier.d(tag = "onError catch", message = e.message.toString())
                _state.update { rState ->
                    rState.copy(
                        error = NetworkError.NO_INTERNET.errorDescription,
                        isLoading = false
                    )
                }
            }

        }
    }

    suspend fun getRandomQuote() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Napier.d(tag = "coroutine scope", message = "inside")
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        quote = ""
                    )
                }

                zenQuotesRepository.getRandomQuote()
                    .onSuccess {
                        _state.update { rState ->
                            rState.copy(
                                isLoading = false,
                                quote = it ?: "no data provided"
                            )
                        }

                        //DataStoreHelper.updateQuote(it)
                        Napier.d(tag = "onSuccess", message = it ?: "empty success")
                    }
                    .onError {
                        Napier.d(tag = "onError", message = it.toString())
                        _state.update { rState ->
                            rState.copy(
                                error = it.errorDescription,
                                isLoading = false
                            )
                        }
                    }
            } catch (e: Exception) {
                Napier.d(tag = "onError catch", message = e.message.toString())
                _state.update { rState ->
                    rState.copy(
                        error = NetworkError.NO_INTERNET.errorDescription,
                        isLoading = false
                    )
                }
            }

        }
    }
}

