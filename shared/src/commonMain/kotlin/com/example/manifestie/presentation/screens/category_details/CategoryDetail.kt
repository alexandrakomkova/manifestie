package com.example.manifestie.presentation.screens.category_details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    onUpClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable {

            }
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Details")

        Text(
            text = "Back",
            modifier = Modifier.clickable {
                onUpClick()
            }
        )
    }
}