package com.example.manifestie.presentation.screens.category_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manifestie.core.ErrorBox
import com.example.manifestie.domain.model.Category
import com.example.manifestie.presentation.screens.category_list.add_category.AddCategorySheet
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
   // onCategoryClick: (Category) -> Unit,
    dialogState: AddCategoryState,
    viewModel: CategoryListViewModel,
    onEvent: (AddCategoryEvent) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.getCategories()
        Napier.d(tag = "CategoryScreen - LaunchedEffect", message = state.toString())
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(AddCategoryEvent.OnAddCategoryClick)
                },
                shape = RoundedCornerShape(15.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "add_category"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValue ->

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> {
                    Napier.d(tag = "CategoryListScreen", message = "loading")
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Napier.d(tag = "CategoryListScreen", message = "error")

                    ErrorBox (
                        errorDescription = state.error.toString(),
                        onTryAgainClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.getCategories()
                            }
                        }
                    )
                }
                else -> {
                    AnimatedVisibility(
                        visible = state.categories.isEmpty(),
                        enter =  scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "Add your first category!",
                            color = Color.Black,
                            fontSize = 22.sp)
                    }
                    AnimatedVisibility(
                        visible = state.categories.isNotEmpty(),
                        enter =  scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        CategoryListBlock(
                            paddingValue = paddingValue,
                            state = state,
                            onEvent = onEvent
                        )
                    }
                }
            }
        }

        if(dialogState.dialogOpen) {
            ModalBottomSheet(
                onDismissRequest = {
                    onEvent(AddCategoryEvent.OnCategoryDialogDismiss)
                },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 16.dp,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary)
                            //.align(Alignment.Center)
                    )
                }
            ) {
                AddCategorySheet(
                    state = dialogState,
                    onEvent = { event -> onEvent(event) }
                )
            }
        }
    }
}

@Composable
fun CategoryListBlock(
    modifier: Modifier = Modifier,
    paddingValue: PaddingValues,
    state: CategoryListState,
    onEvent: (AddCategoryEvent) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .padding(paddingValue),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp
    ) {
        items(state.categories) {category ->
            CategoryCard(
                modifier = modifier,
                category = category,
                onEvent = { event -> onEvent(event) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    category: Category,
    onEvent: (AddCategoryEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    Napier.d(tag = "CategoryListScreen card", message = "on click")
                },
                onLongClick = {
                    Napier.d(tag = "CategoryListScreen card", message = "on long click")
                    expanded = !expanded
                }
            ),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xff955999),
                            Color(0xff454589)
                        )
                    )
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            DropdownMenu(
                modifier = Modifier.align(Alignment.TopEnd),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        Napier.d(tag = "dropdown edit", message = "edit")
                        onEvent(AddCategoryEvent.SelectCategory(category))
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        Napier.d(tag = "dropdown", message = "delete")
                        onEvent(AddCategoryEvent.SelectCategory(category))
                        onEvent(AddCategoryEvent.DeleteCategory)
                    }
                )
            }

            Text(
                text = category.title,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                minLines = 1,
                maxLines = 2,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
        }
    }

}