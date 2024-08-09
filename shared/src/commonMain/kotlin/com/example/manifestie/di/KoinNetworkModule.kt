package com.example.manifestie.di

import com.example.manifestie.data.network.UnsplashClient
import com.example.manifestie.data.network.ZenQuotesClient
import com.example.manifestie.data.network.createHttpClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {

    single { provideHttpClient() }
    single { provideZenQuotesClient(get()) }
    single { provideUnsplashClient(get()) }
}

fun provideHttpClient(): HttpClient {
    return createHttpClient()
}

fun provideZenQuotesClient(httpClient: HttpClient): ZenQuotesClient {
    return ZenQuotesClient(httpClient)
}

fun provideUnsplashClient(httpClient: HttpClient): UnsplashClient {
    return UnsplashClient(httpClient)
}