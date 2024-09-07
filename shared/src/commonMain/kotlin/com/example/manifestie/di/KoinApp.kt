package com.example.manifestie.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module


fun getBaseModules() = listOf(
    dataModule,
    networkModule,
    appModule,
    platformModule
)

fun initKoin(additionalModules: List<Module>) {
    startKoin {
        modules(additionalModules + getBaseModules())
    }
}


