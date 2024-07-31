package com.example.manifestie.di

import com.example.manifestie.data.ZenQuotesRepositoryImpl
import com.example.manifestie.domain.repository.ZenQuotesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun appModule() = module {
    singleOf(::ZenQuotesRepositoryImpl) {
        bind<ZenQuotesRepository>()
    }
}

