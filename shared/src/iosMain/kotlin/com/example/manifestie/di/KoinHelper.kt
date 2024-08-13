package com.example.manifestie.di

import org.koin.core.context.startKoin

fun initKoinIOS() {
    startKoin {
        modules(getBaseModules())
    }
}