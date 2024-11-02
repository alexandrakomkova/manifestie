package com.example.manifestie.presentation.screens.category.category_list

import com.example.manifestie.data.repository.FirestoreCategorySharedRepositoryImpl
import com.example.manifestie.domain.model.Category
import com.example.manifestie.presentation.screens.category.CategorySharedViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch


class CategoryViewModelImpl(
    private val viewModel: CategorySharedViewModel,
    private val firestoreCategorySharedRepositoryImpl: FirestoreCategorySharedRepositoryImpl
): CategoryViewModel {
    override fun addCategory(category: Category) {
        viewModel.viewModelScope.launch {
            try {
                firestoreCategorySharedRepositoryImpl.addCategory(category = category)
                Napier.d(tag = "addCategory", message = category.toString())
            } catch (e: Exception) {
                Napier.d(tag = "onError addCategory", message = e.message.toString())
            }
        }
    }

    override fun updateCategory(category: Category) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreCategorySharedRepositoryImpl.updateCategory(category)
            } catch (e: Exception) {
                Napier.d(tag = "onError updateCategory", message = e.message.toString())


            }
        }
    }

    override fun deleteCategory(category: Category) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreCategorySharedRepositoryImpl.deleteCategory(category)
                Napier.d(tag = "deleteCategory", message = category.toString())
            } catch (e: Exception) {
                Napier.d(tag = "onError deleteCategory", message = e.message.toString())

            }
        }
    }
}