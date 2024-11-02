package com.example.manifestie.presentation.screens.category.category_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manifestie.presentation.screens.category.AddQuoteEvent
import com.example.manifestie.presentation.screens.category.AddQuoteSheetState
import com.example.manifestie.presentation.screens.category.CategorySharedState
import com.example.manifestie.presentation.screens.components.CustomTextField

@Composable
fun AddQuoteSheet(
    modifier: Modifier = Modifier,
    addQuoteState: AddQuoteSheetState,
    state: CategorySharedState,
    onEvent: (AddQuoteEvent) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 60.dp)
    ) {
        CustomTextField(
            labelText = "Quote",
            title = addQuoteState.quote,
            setTitle = { onEvent(AddQuoteEvent.OnQuoteContentChanged(it)) } ,
            maxLines = 5,
            hasError = addQuoteState.quoteError != null,
            errorMessage = addQuoteState.quoteError
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                onEvent(AddQuoteEvent.SaveQuote)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp),
        ) {
            Text(
                text = if(state.selectedQuote == null ) "Save" else "Update",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}