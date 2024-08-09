package com.example.manifestie.android.glance_app_widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.example.manifestie.android.MainActivity

class QuoteWidget: GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            RandomWidgetContent()
        }
    }
}

class QuoteWidgetReceiver() : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = QuoteWidget()
}

@Composable
fun RandomWidgetContent(modifier: GlanceModifier = GlanceModifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "When you know what you want, and want it bad enough, you will find a way to get it.",
            maxLines = 7,
            modifier = modifier.fillMaxSize().padding(10.dp)
        )
        Spacer(GlanceModifier.height(5.dp))
        Button(
            text = "Home",
            onClick = actionStartActivity<MainActivity>()
        )
    }
}