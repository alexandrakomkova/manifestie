package com.example.manifestie.data.repository

import com.example.manifestie.core.FIRESTORE_CATEGORY_LIST
import com.example.manifestie.core.FIRESTORE_QUOTE_LIST
import com.example.manifestie.domain.model.Category
import com.example.manifestie.domain.model.Quote
import com.example.manifestie.domain.repository.CategorySharedRepository
import com.example.manifestie.domain.repository.QuotesRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirestoreCategorySharedRepositoryImpl: CategorySharedRepository, QuotesRepository {
    private val firestore = Firebase.firestore

    override fun getCategories() = flow {
        firestore.collection(FIRESTORE_CATEGORY_LIST).snapshots.collect { querySnapshot ->
            val categoryList = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<Category>()
            }
            emit(categoryList)
        }
    }

    override fun getCategoryById(id: String): Flow<Category> = flow {
        firestore.collection(FIRESTORE_CATEGORY_LIST).document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<Category>())
        }
    }

    override suspend fun addCategory(category: Category) {
        val categoryId = generateRandomStringId()

        firestore.collection(FIRESTORE_CATEGORY_LIST)
            .document(categoryId)
            .set(category.copy(id = categoryId))
    }

    override suspend fun updateCategory(category: Category) {
        firestore.collection(FIRESTORE_CATEGORY_LIST).document(category.id).set(category)
    }

    override suspend fun deleteCategory(category: Category) {
        firestore.collection(FIRESTORE_CATEGORY_LIST).document(category.id).delete()
    }

    private fun generateRandomStringId(length: Int = 20): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override fun getQuotesByCategoryId(categoryId: String): Flow<List<Quote>> = flow {
        firestore
            .collection(FIRESTORE_CATEGORY_LIST)
            .document(categoryId)
            .collection(FIRESTORE_QUOTE_LIST)
            .snapshots.collect { querySnapshot ->
                val quoteList = querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.data<Quote>()
                }
                emit(quoteList)
            }
    }

    override suspend fun addQuoteToCategory(quote: Quote, categoryId: String) {
        val quoteId = generateRandomStringId()

        firestore.collection(FIRESTORE_CATEGORY_LIST)
            .document(categoryId)
            .collection(FIRESTORE_QUOTE_LIST)
            .document(quoteId)
            .set(quote.copy(id = quoteId))
    }

    override suspend fun updateQuoteFromCategory(quote: Quote, categoryId: String) {
        firestore.collection(FIRESTORE_CATEGORY_LIST)
            .document(categoryId)
            .collection(FIRESTORE_QUOTE_LIST)
            .document(quote.id)
            .set(quote)
    }

    override suspend fun deleteQuoteFromCategory(quote: Quote, categoryId: String) {
        firestore.collection(FIRESTORE_CATEGORY_LIST)
            .document(categoryId)
            .collection(FIRESTORE_QUOTE_LIST)
            .document(quote.id)
            .delete()
    }
}