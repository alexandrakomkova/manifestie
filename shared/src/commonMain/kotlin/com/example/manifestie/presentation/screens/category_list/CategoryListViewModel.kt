package com.example.manifestie.presentation.screens.category_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.manifestie.core.NetworkError
import com.example.manifestie.presentation.screens.category_list.add_category.AddCategoryDialogEvent
import com.example.manifestie.presentation.screens.category_list.add_category.AddCategoryDialogState
import com.example.manifestie.presentation.screens.category_list.add_category.AddCategoryTitleValidation
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CategoryListViewModel(

): ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(CategoryListState())
    val state = _state.asStateFlow()

    var addCategoryState by mutableStateOf(AddCategoryDialogState())
    private val validateCategoryTitle: AddCategoryTitleValidation by inject()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: AddCategoryDialogEvent) {
        when(event) {
            is AddCategoryDialogEvent.CategoryTitleChanged -> {
                addCategoryState = addCategoryState.copy(title = event.title)
            }
            is AddCategoryDialogEvent.CategoryDialogOpened -> {
                addCategoryState = addCategoryState.copy(dialogOpen = event.dialogOpened)
            }
            is AddCategoryDialogEvent.Submit -> { submitData() }
        }

    }

    private fun submitData() {
        val titleResult = validateCategoryTitle.execute(addCategoryState.title)
        val hasError = listOf(
            titleResult
        ).any { !it.successful }

        if(hasError) {
            addCategoryState = addCategoryState.copy(
                titleError = titleResult.errorMessage,
                dialogOpen = true
            )
            return
        }

        viewModelScope.launch { validationEventChannel.send(ValidationEvent.Success) }
    }

    suspend fun getCategoryList() {
        val firebaseFirestore = Firebase.firestore

        CoroutineScope(Dispatchers.IO).launch {
             try {
                 _state.update {
                     it.copy(
                         isLoading = true,
                         error = null,
                         categories = emptyList()
                     )
                 }

                 val categoryResponse = firebaseFirestore
                     .collection("CATEGORIES")
                     .get()

                 _state.update { categoryListState ->
                     categoryListState.copy(
                         isLoading = false,
                         error = null,
                         categories = categoryResponse.documents.map {
                             it.data()
                         }
                     )
                 }

             } catch (e: Exception) {
                 Napier.d(tag = "onError catch", message = e.message.toString())
                 _state.update { categoryListState ->
                     categoryListState.copy(
                         error = NetworkError.NO_INTERNET.errorDescription,
                         isLoading = false
                     )
                 }
             }
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

}