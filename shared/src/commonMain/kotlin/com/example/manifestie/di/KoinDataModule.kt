package com.example.manifestie.di

import com.example.manifestie.data.ZenQuotesRepositoryImpl
import com.example.manifestie.domain.repository.ZenQuotesRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        ZenQuotesRepositoryImpl (zenQuotesClient = get())
    } bind ZenQuotesRepository::class
}