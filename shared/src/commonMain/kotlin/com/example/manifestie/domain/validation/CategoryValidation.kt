package com.example.manifestie.domain.validation

object CategoryValidation {

    fun validateCategoryTitle(title: String): ValidationResult {
        var result = ValidationResult()

        if(title.isBlank()) {
            result = result.copy(categoryTitleError = "Title can't be empty.")
        } else if (title.length < 2) {
            result = result.copy(categoryTitleError = "Title must be at least 2 characters long.")
        }

        return result
    }

    data class ValidationResult(
        val categoryTitleError: String? = null,
    )
}