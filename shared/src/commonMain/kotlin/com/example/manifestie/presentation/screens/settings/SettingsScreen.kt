package com.example.manifestie.presentation.screens.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.toArgb
import com.example.manifestie.resources.Res
import com.example.manifestie.resources.settings_choose_color
import com.example.manifestie.resources.settings_save
import com.example.manifestie.resources.settings_timepicker_label
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    onEvent: (SettingsEvent) -> Unit
) {
    var selectedColor by remember { mutableStateOf(Color.Red) }
    var selectedTime by remember { mutableStateOf("10:00") }

    val state by viewModel.state.collectAsState()
    val controller = rememberColorPickerController()

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = stringResource(Res.string.settings_choose_color),
                style = MaterialTheme.typography.titleSmall
            )

            Box(modifier = Modifier.padding(vertical = 6.dp)) {
                AlphaTile(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            CircleShape
                        ),
                    controller = controller
                )
            }

            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope ->
                    selectedColor = colorEnvelope.color
                },
                initialColor = selectedColor
            )

            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .padding(bottom = 10.dp),
                controller = controller,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            TimeSettingSection(
                currentTime = selectedTime,
                onTimeChange = { newTime ->
                    if (newTime.matches(Regex("\\d{0,2}:\\d{0,2}"))) {
                        selectedTime = newTime
                    }
                },
                onTimeSet = { finalTime ->
                    println("Установлено время: $finalTime")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val finalColor = controller.selectedColor.value

                    println("Сохранение настроек: Цвет=${finalColor}, Время=${state.selectedTime}")
                    println("Сохраненный Hex: ${finalColor.toHexCode()}")

                    onEvent(SettingsEvent.SaveSettings(
                        selectedTime = selectedTime,
                        selectedColor = finalColor.toHexCode()
                    ))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(15.dp),
            ) {
                Text(stringResource(Res.string.settings_save))
            }
        }
    }
}

@Composable
fun TimeSettingSection(
    currentTime: String,
    onTimeChange: (String) -> Unit,
    onTimeSet: (String) -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.settings_timepicker_label),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(end = 8.dp)
        )

        OutlinedTextField(
            value = currentTime,
            onValueChange = onTimeChange,
            label = { Text(text = stringResource(Res.string.settings_timepicker_label)) },
            singleLine = true,
            modifier = Modifier.width(120.dp)
        )
    }
}

fun Color.toHexCode(includeAlpha: Boolean = false): String {
    val argb = this.toArgb()

    return if (includeAlpha) {
        "#${argb.toUInt().toString(16).padStart(8, '0').uppercase()}"
    } else {
        "#${(argb and 0xFFFFFF).toUInt().toString(16).padStart(6, '0').uppercase()}"
    }
}



