package com.example.manifestie.presentation.screens.category_list.add_category

sealed class AddCategoryDialogEvent {
    data class CategoryTitleChanged(val title: String): AddCategoryDialogEvent()
    data class CategoryDialogOpened(val dialogOpened: Boolean): AddCategoryDialogEvent()

    object Submit: AddCategoryDialogEvent()
}