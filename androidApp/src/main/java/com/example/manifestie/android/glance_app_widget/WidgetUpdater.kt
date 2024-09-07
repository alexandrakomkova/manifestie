package com.example.manifestie.android.glance_app_widget

import androidx.glance.appwidget.GlanceAppWidgetManager
import com.example.manifestie.android.MyApplication

class WidgetUpdater {
    private val appContext = MyApplication.applicationContext
    private val manager = GlanceAppWidgetManager(appContext)

    suspend fun saveWidgetStateByManager(quote: String) {
        manager.getGlanceIds(QuoteWidget::class.java).forEach { id ->
            QuoteWidget(quote).update(appContext, id)
        }
    }
}