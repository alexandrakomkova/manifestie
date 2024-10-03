package com.example.manifestie.presentation.screens.category_list.add_category

class AddCategoryTitleValidation {
    fun execute(title: String): ValidationResult {
        if(title.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Title should not be empty"
            )
        }


        return ValidationResult(
            successful = true
        )
    }
}

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)