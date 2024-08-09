package com.example.manifestie.di

import com.example.manifestie.data.repository.UnsplashRepositoryImpl
import com.example.manifestie.data.repository.ZenQuotesRepositoryImpl
import com.example.manifestie.domain.repository.UnsplashRepository
import com.example.manifestie.domain.repository.ZenQuotesRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        ZenQuotesRepositoryImpl (zenQuotesClient = get())
    } bind ZenQuotesRepository::class

    single {
        UnsplashRepositoryImpl (unsplashClient = get())
    } bind UnsplashRepository::class
}