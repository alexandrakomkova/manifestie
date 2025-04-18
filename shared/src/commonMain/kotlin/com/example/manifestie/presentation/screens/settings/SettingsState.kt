package com.example.manifestie.presentation.screens.settings

import androidx.compose.ui.graphics.Color

data class SettingsState (
    val selectedColor: String = "#000000",
    val selectedTime: String = "10:00"
)

sealed interface SettingsEvent {
    data class SelectColor(val selectedColor: String): SettingsEvent
    data class OnTimeChanged(val selectedTime: String): SettingsEvent

    data object SaveSettings: SettingsEvent
}