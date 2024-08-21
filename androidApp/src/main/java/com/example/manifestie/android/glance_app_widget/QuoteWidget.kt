package com.example.manifestie.android.glance_app_widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.example.manifestie.android.MainActivity
import com.example.manifestie.data.datastore.DataStoreHelper

class QuoteWidget(
    private var quote: String = "When you know what you want, and want it bad enough, you will find a way to get it."
): GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

       // quote = DataStoreHelper.quotePreferencesFlow.first() // IT WORKS
//        Log.d("QuoteWidget", quote)
//        DataStoreHelper.quoteUpdate()
//        quote = DataStoreHelper.quote.value

        Log.d("QuoteWidget", DataStoreHelper.readFromDataStore())

        provideContent {
            // RandomWidgetContent(quote = quoteWidgetFlow ?: quote)
            RandomWidgetContent(quote = DataStoreHelper.readFromDataStore())
        }
    }
}

class QuoteWidgetReceiver() : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = QuoteWidget()
}

@Composable
fun RandomWidgetContent(
    modifier: GlanceModifier = GlanceModifier,
    quote: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .clickable(actionStartActivity<MainActivity>()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = quote,
            maxLines = 7,
            modifier = modifier.fillMaxSize().padding(10.dp)
        )
    }
}