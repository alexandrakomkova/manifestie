package com.example.manifestie.data.repository

import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.repository.CategoryRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirestoreCategoryRepositoryImpl: CategoryRepository {
    private val firestore = Firebase.firestore

    override fun getCategories() = flow {
        firestore.collection("CATEGORIES").snapshots.collect { querySnapshot ->
            val categoryList = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Category>()
            }
            emit(categoryList)
        }
    }

    override fun getCategoryById(id: String): Flow<Category> = flow {
        firestore.collection("CATEGORIES").document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<Category>())
        }
    }

    override suspend fun addCategory(category: Category) {
        val categoryId = generateRandomStringId()
        firestore.collection("CATEGORIES")
            .document(categoryId)
            .set(category.copy(id = categoryId))
    }

    override suspend fun updateCategory(category: Category) {
        firestore.collection("CATEGORIES").document(category.id).set(category)
    }

    override suspend fun deleteCategory(category: Category) {
        firestore.collection("CATEGORIES").document(category.id).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}