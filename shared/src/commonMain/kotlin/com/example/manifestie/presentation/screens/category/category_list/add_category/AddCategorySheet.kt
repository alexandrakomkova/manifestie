package com.example.manifestie.presentation.screens.category.category_list.add_category

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
import com.example.manifestie.presentation.screens.category.AddCategoryEvent
import com.example.manifestie.presentation.screens.category.AddCategoryState

@Composable
fun AddCategorySheet(
    modifier: Modifier = Modifier,
    state: AddCategoryState,
    onEvent: (AddCategoryEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 60.dp),
    ) {

        CustomTextField(
            labelText = "Title",
            title = state.title,
            setTitle = { onEvent(AddCategoryEvent.OnCategoryTitleChanged(it)) } ,
            maxLines = 20,
            hasError = state.titleError != null,
            errorMessage = state.titleError
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                onEvent(AddCategoryEvent.SaveCategory)
//                if(state.selectedCategory != null) {
//                    onEvent(AddCategoryEvent.EditCategory(state.selectedCategory))
//                } else {
//                    onEvent(AddCategoryEvent.SaveCategory)
//                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp),
        ) {
            Text(
                text = if(state.selectedCategory == null ) "Save" else "Update",
                color = Color.White,
                fontSize = 16.sp
            )
        }

    }
}

