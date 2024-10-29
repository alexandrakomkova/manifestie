package com.example.manifestie.di

import com.example.manifestie.presentation.screens.category.CategorySharedViewModel
import com.example.manifestie.presentation.screens.random_quote.RandomQuoteViewModel
import org.koin.dsl.module

val appModule = module {
    single {
        RandomQuoteViewModel(
            zenQuotesRepository = get(),
            unsplashRepository = get(),
            firestoreCategorySharedRepositoryImpl = get()
        )
    }

    single {
        CategorySharedViewModel(
            firestoreCategorySharedRepositoryImpl = get()
        )
    }
}