package com.example.manifestie.presentation.screens.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(): ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.OnTimeChanged -> updateTime(event.selectedTime)
            is SettingsEvent.SelectColor -> updateSelectedColor(event.selectedColor)
            SettingsEvent.SaveSettings -> saveSettings()
        }
    }

    private fun saveSettings() {

    }

    private fun updateSelectedColor(selectedColor: String) {
        _state.update { it.copy(
            selectedColor = selectedColor
        ) }
    }

    private fun updateTime(selectedTime: String) {
        _state.update { it.copy(
            selectedTime = selectedTime
        ) }
    }
}