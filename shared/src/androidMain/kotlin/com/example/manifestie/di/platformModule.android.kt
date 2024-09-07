package com.example.manifestie.di

import com.example.manifestie.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { createDataStore(get()) }
}