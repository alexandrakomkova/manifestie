package com.example.manifestie.di

import com.example.manifestie.data.network.ZenQuotesClient
import com.example.manifestie.data.network.createHttpClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {

    single { provideHttpClient() }
    single { provideZenQuotesClient(get()) }
}

fun provideHttpClient(): HttpClient {
    return createHttpClient()

}

fun provideZenQuotesClient(httpClient: HttpClient): ZenQuotesClient {
    return ZenQuotesClient(httpClient)
}