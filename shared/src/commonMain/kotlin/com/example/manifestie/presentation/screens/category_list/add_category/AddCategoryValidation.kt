package com.example.manifestie.presentation.screens.category_list.add_category

//class AddCategoryTitleValidation {
//    fun execute(title: String): ValidationResult {
//        if(title.isBlank()) {
//            return ValidationResult(
//                successful = false,
//                errorMessage = "Title should not be empty"
//            )
//        }
//
//
//        return ValidationResult(
//            successful = true
//        )
//    }
//}
//
//data class ValidationResult(
//    val successful: Boolean,
//    val errorMessage: String? = null
//)

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