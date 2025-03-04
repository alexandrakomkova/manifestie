package com.example.manifestie.presentation.screens.random_quote

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.onError
import com.example.manifestie.core.onSuccess
import com.example.manifestie.data.datastore.DataStoreHelper
import com.example.manifestie.data.repository.FirestoreCategorySharedRepositoryImpl
import com.example.manifestie.data.repository.UnsplashRepositoryImpl
import com.example.manifestie.data.repository.ZenQuotesRepositoryImpl
import com.example.manifestie.domain.model.Quote
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
    private val firestoreCategorySharedRepositoryImpl: FirestoreCategorySharedRepositoryImpl
): ViewModel() {

    private val _state = MutableStateFlow(RandomQuoteState())
    val state = _state.asStateFlow()

    private val _chooseCategoryState = MutableStateFlow(ChooseCategoryState())
    val chooseCategoryState = _chooseCategoryState.asStateFlow()

    private val randomQuoteEventHandler = RandomQuoteEventHandler(this)

    fun updateChooseCategoryState(update: (ChooseCategoryState) -> ChooseCategoryState) {
        _chooseCategoryState.update { update(it) }
    }

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
                        
                        //DataStoreHelper.updateQuote(it)
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

                        DataStoreHelper.updateQuote(it)
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

    fun onEvent(event: RandomQuoteEvent) {
        when(event) {
            RandomQuoteEvent.OnLikeQuoteClick -> randomQuoteEventHandler.handleOnLikeQuoteClick()
            RandomQuoteEvent.OnChooseCategorySheetDismiss -> randomQuoteEventHandler.handleOnChooseCategorySheetDismiss()
            is RandomQuoteEvent.SelectCategory -> randomQuoteEventHandler.handleSelectCategory(event)
            RandomQuoteEvent.SaveQuoteToCategory -> randomQuoteEventHandler.handleSaveQuoteToCategory()
        }
    }

    fun addQuoteToCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(chooseCategoryState.value.selectedCategory != null) {
                    firestoreCategorySharedRepositoryImpl.addQuoteToCategory(
                        quote = Quote(quote = state.value.quote),
                        categoryId = chooseCategoryState.value.selectedCategory!!.id
                    )
                }

                Napier.d(tag = "addQuoteToCategory", message = "${state.value.quote} ADDED TO ${chooseCategoryState.value.selectedCategory!!.id}")
            } catch (e: Exception) {
                Napier.d(tag = "onError addQuoteToCategory", message = e.message.toString())
            }
        }
    }
}

