package com.example.manifestie.domain.validation

object QuoteValidation {

    fun validateQuoteContent(quote: String): ValidationResult {
        var result = ValidationResult()

        if(quote.isBlank()) {
            result = result.copy(quoteContentError = "Quote can't be empty.")
        } else if (quote.isEmpty()) {
            result = result.copy(quoteContentError = "Quote must be at least 1 characters long.")
        }

        return result
    }

    data class ValidationResult(
        val quoteContentError: String? = null,
    )
}


