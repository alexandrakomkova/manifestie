package com.example.manifestie.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.manifestie.App
import com.example.manifestie.di.initKoin
import org.koin.dsl.module


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin(
            listOf(
                module {
                    single<Context> { this@MainActivity }
                }
            )
        )
        setContent {
            App()
        }
    }
}


