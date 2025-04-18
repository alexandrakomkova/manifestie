package com.example.manifestie.presentation.screens.settings

data class SettingsState (
    val selectedColor: String = "#000000",
    val selectedTime: String = "10:00"
)

sealed interface SettingsEvent {
    data class SelectColor(val selectedColor: String): SettingsEvent
    data class OnTimeChanged(val selectedTime: String): SettingsEvent

    data class SaveSettings(val selectedColor: String, val selectedTime: String): SettingsEvent
}