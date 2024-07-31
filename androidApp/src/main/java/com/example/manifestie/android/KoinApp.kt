package com.example.manifestie.android

import android.app.Application
import com.example.manifestie.android.di.androidModule
import com.example.manifestie.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KoinApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KoinApp)
            androidLogger()
            modules(appModule() + androidModule())
        }
    }
}