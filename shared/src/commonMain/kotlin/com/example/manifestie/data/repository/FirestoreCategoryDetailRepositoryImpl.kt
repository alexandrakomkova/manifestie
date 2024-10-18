package com.example.manifestie.data.repository

import com.example.manifestie.core.FIRESTORE_CATEGORY_LIST
import com.example.manifestie.core.FIRESTORE_QUOTE_LIST
import com.example.manifestie.domain.model.Quote
import com.example.manifestie.domain.repository.CategoryDetailRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirestoreCategoryDetailRepositoryImpl: CategoryDetailRepository {
    private val firestore = Firebase.firestore

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

}