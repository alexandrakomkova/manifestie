package com.example.manifestie.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.manifestie.App
import com.example.manifestie.android.glance_app_widget.WidgetUpdater
import com.example.manifestie.data.datastore.DataStoreHelper
import com.example.manifestie.di.initKoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

        lifecycleScope.launch {
            WidgetUpdater().saveWidgetStateByManager(DataStoreHelper.quotePreferencesFlow.first())
        }

        setContent {
            App()
        }
    }
}


