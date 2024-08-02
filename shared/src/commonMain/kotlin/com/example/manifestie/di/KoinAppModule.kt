package com.example.manifestie.di

import com.example.manifestie.presentation.RandomQuoteViewModel
import org.koin.dsl.module

val appModule = module {
    single {
        RandomQuoteViewModel(repository = get())
    }
}