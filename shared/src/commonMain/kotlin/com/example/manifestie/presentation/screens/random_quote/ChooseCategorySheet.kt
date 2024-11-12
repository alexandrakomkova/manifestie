package com.example.manifestie.presentation.screens.random_quote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manifestie.domain.model.Category
import com.example.manifestie.presentation.screens.category.CategorySharedState
import io.github.aakira.napier.Napier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseCategorySheet(
    modifier: Modifier = Modifier,
    onEvent: (RandomQuoteEvent) -> Unit,
    chooseCategoryState: ChooseCategoryState,
    sharedState: CategorySharedState
) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 60.dp)
        ) {
//            val options: List<Category> = listOf(
//                Category(title = "family"),
//                Category(title = "love"),
//                Category(title = "friendship"),
//                Category(title = "kindness"),
//                Category(title = "work and success"),
//            )

            val options: List<Category> = sharedState.categories
            Napier.d(tag = "ChooseCategorySheet", message = options.toString())

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange =  { expanded = it }
            ) {

//                val result = when(chooseCategoryState.selectedCategory == null) {
//                    true -> options.first().title
//                    false -> chooseCategoryState.selectedCategory.title
//                }

                if(chooseCategoryState.selectedCategory == null) {
                    onEvent(RandomQuoteEvent.SelectCategory(options.first()))
                }

                chooseCategoryState.selectedCategory?.let {
                    TextField(
                        value = it.title,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = modifier.menuAnchor().fillMaxWidth(),
                        label = { Text(text = "Select category") }
                    )
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.title, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                expanded = false
                                onEvent(RandomQuoteEvent.SelectCategory(option))
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Spacer(modifier = modifier.height(15.dp))

            Button(
                onClick = {
                    onEvent(RandomQuoteEvent.SaveQuoteToCategory)
                },
                modifier = modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(15.dp),
            ) {
                Text(
                    text = "Save affirmation",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
}
