package com.example.manifestie.android.di

import com.example.manifestie.network.ZenQuotesClient
import com.example.manifestie.network.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module



fun androidModule() = module {

    single { provideHttpClient() }
    single { provideZenQuotesClient(get()) }
}

fun provideHttpClient(): HttpClient {
    return createHttpClient(OkHttp.create())
}

fun provideZenQuotesClient(httpClient: HttpClient): ZenQuotesClient {
    return ZenQuotesClient(httpClient)
}