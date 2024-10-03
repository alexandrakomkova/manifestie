package com.example.manifestie.presentation.screens.category_list.add_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    labelText: String,
    title: String,
    setTitle: (String) -> Unit,
    maxLines: Int,
    hasError: Boolean,
    errorMessage: String?
) {
    Column (
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { setTitle(it) },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(text = labelText)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                textColor = Color.White
            ),
            maxLines = maxLines,
            shape = RoundedCornerShape(15.dp),
            isError = hasError

        )
        Spacer(modifier = Modifier.height(5.dp))
        if(hasError) {
            Text(
                text = errorMessage?: "",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
            )
        }
    }
}