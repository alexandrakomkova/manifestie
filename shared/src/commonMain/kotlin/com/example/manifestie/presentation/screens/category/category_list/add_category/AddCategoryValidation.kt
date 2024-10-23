package com.example.manifestie.presentation.screens.category.category_list.add_category

object CategoryValidation {

    fun validateCategoryTitle(title: String): ValidationResult {
        var result = ValidationResult()

        if(title.isBlank()) {
            result = result.copy(categoryTitleError = "Title can't be empty.")
        }

        return result
    }

    data class ValidationResult(
        val categoryTitleError: String? = null,
    )
}