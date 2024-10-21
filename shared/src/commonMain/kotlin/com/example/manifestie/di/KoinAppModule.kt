package com.example.manifestie.di

import com.example.manifestie.presentation.screens.category_details.CategoryDetailViewModel
import com.example.manifestie.presentation.screens.category_list.CategoryListViewModel
import com.example.manifestie.presentation.screens.random_quote.RandomQuoteViewModel
import org.koin.dsl.module

val appModule = module {
    single {
        RandomQuoteViewModel(
            zenQuotesRepository = get(),
            unsplashRepository = get()
        )
    }

    single {
       CategoryListViewModel(firestoreCategoryRepositoryImpl = get())
    }

    single {
        CategoryDetailViewModel(
            firestoreCategoryDetailRepositoryImpl = get(),
            savedStateHandle = get()
        )
    }
}