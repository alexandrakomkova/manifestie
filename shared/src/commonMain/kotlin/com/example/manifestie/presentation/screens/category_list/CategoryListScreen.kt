package com.example.manifestie.presentation.screens.category_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manifestie.core.ErrorBox
import com.example.manifestie.presentation.screens.category_list.add_category.AddCategorySheet
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
   // onCategoryClick: (Category) -> Unit,
    dialogState: AddCategoryState,
    viewModel: CategoryListViewModel,
    onEvent: (AddCategoryEvent) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCategoryList()
        Napier.d(tag = "CategoryScreen - LaunchedEffect", message = state.toString())

//        viewModel.validationEvents.collect { event ->
//            when(event) {
//                is CategoryListViewModel.ValidationEvent.Success -> {
//                    // add catego
//                    //viewModel.addCategory(dialogState.title)
//                    Napier.d(tag = "CategoryScreen - when", message = dialogState.toString())
//
//
//                    viewModel.onEvent(AddCategoryDialogEvent.CategoryDialogOpened(false))
//                    viewModel.onEvent(AddCategoryDialogEvent.CategoryTitleChanged(""))
//                }
//            }
//        }
    }

//    if(dialogState.dialogOpen) {
//        Dialog(
//            onDismissRequest = {
//                onEvent(AddCategoryDialogEvent.CategoryDialogOpened(false))
//            }
//        ) {
//            Column(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(Color.LightGray)
//                    .padding(10.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                CustomTextField(
//                    "Title",
//                    dialogState.title,
//                    { onEvent(AddCategoryDialogEvent.CategoryTitleChanged(it)) } ,
//                    20,
//                    dialogState.titleError != null,
//                    dialogState.titleError
//                )
//                Spacer(modifier = Modifier.height(15.dp))
//
//                Button(
//                    onClick = { onEvent(AddCategoryDialogEvent.Submit) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(55.dp),
//                    shape = RoundedCornerShape(15.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.White
//                    )
//                ) {
//                    Text(
//                        text = "Save",
//                        color = Color.DarkGray,
//                        fontSize = 16.sp
//                    )
//                }
//            }
//        }
//    }

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
        }
    ) { paddingValue ->

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    Napier.d(tag = "RandomQuoteScreen", message = "loading")
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Napier.d(tag = "CategoryListScreen", message = "error")

                    ErrorBox (
                        errorDescription = state.error.toString(),
                        onTryAgainClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.getCategoryList()
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
                            text = "Add your first category!",
                            color = Color.White,
                            fontSize = 22.sp)
                    }
                    AnimatedVisibility(
                        visible = state.categories.isNotEmpty(),
                        enter =  scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        CategoryListBlock(
                            paddingValue = paddingValue,
                            state = state
                        )
                    }
                }
            }
        }

        AddCategorySheet(
            state = dialogState,
            isOpen = dialogState.dialogOpen,
            onEvent = { event -> onEvent(event)}
        )
    }
}

@Composable
fun CategoryListBlock(
    modifier: Modifier = Modifier,
    paddingValue: PaddingValues,
    state: CategoryListState
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
                modifier,
                category.title
            )
        }
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    categoryName: String = "Love and friendship"
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
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
            Text(
                text = categoryName,
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