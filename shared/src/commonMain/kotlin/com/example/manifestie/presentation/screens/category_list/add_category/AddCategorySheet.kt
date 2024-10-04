package com.example.manifestie.presentation.screens.category_list.add_category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manifestie.presentation.screens.category_list.AddCategoryEvent
import com.example.manifestie.presentation.screens.category_list.AddCategoryState

@Composable
fun AddCategorySheet(
    modifier: Modifier = Modifier,
    state: AddCategoryState,
    isOpen: Boolean,
    onEvent: (AddCategoryEvent) -> Unit
) {
    SimpleBottomSheet(
        modifier = Modifier.fillMaxSize(),
        visible = isOpen
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(80.dp))

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
                    onClick = { onEvent(AddCategoryEvent.SaveCategory) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Text(
                        text = "Save",
                        //color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                }

            }

            IconButton(
                onClick = {
                    onEvent(AddCategoryEvent.OnCategoryDialogDismiss)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close"
                )
            }
        }
    }

}

