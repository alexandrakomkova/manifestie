package com.example.manifestie.presentation.screens.settings

import androidx.lifecycle.ViewModel
import com.example.manifestie.data.datastore.DataStoreHelper
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
            is SettingsEvent.SaveSettings -> saveSettings(
                selectedColor = event.selectedColor,
                selectedTime = event.selectedTime)
        }
    }

    private fun saveSettings(
        selectedColor: String,
        selectedTime: String
    ) {
        DataStoreHelper.updateWidgetTime(selectedTime)
        DataStoreHelper.updateWidgetColor(selectedColor)
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