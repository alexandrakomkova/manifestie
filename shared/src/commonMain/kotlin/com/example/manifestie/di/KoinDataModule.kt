package com.example.manifestie.di

import com.example.manifestie.data.datastore.DataStoreHelper
import com.example.manifestie.data.repository.FirestoreCategoryDetailRepositoryImpl
import com.example.manifestie.data.repository.FirestoreCategoryRepositoryImpl
import com.example.manifestie.data.repository.UnsplashRepositoryImpl
import com.example.manifestie.data.repository.ZenQuotesRepositoryImpl
import com.example.manifestie.domain.repository.CategoryDetailRepository
import com.example.manifestie.domain.repository.CategoryRepository
import com.example.manifestie.domain.repository.UnsplashRepository
import com.example.manifestie.domain.repository.ZenQuotesRepository
import com.example.manifestie.presentation.screens.category_list.add_category.CategoryValidation
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        ZenQuotesRepositoryImpl (zenQuotesClient = get())
    } bind ZenQuotesRepository::class

    single {
        UnsplashRepositoryImpl (unsplashClient = get())
    } bind UnsplashRepository::class

    single {
        FirestoreCategoryRepositoryImpl()
    } bind CategoryRepository::class

    single {
        FirestoreCategoryDetailRepositoryImpl()
    } bind CategoryDetailRepository::class

    single { DataStoreHelper }
    single { CategoryValidation }
}