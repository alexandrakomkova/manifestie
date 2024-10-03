package com.example.manifestie.presentation.screens.category_list

import com.example.manifestie.core.NetworkError
import com.example.manifestie.domain.model.Category
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryListViewModel(

): ViewModel() {
    private val _state = MutableStateFlow(CategoryListState())
    val state = _state.asStateFlow()

    suspend fun getCategories(): List<Category> {
        val firebaseFirestore = Firebase.firestore

        try {
            val categoryResponse = firebaseFirestore
                .collection("CATEGORIES")
                .get()

            return categoryResponse.documents.map {
                it.data()
            }
        } catch (e: Exception) {
            throw e
        }
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

}