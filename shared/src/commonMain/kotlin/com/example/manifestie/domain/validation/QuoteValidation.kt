package com.example.manifestie.domain.validation

object QuoteValidation {

    fun validateQuoteContent(quote: String): ValidationResult {
        var result = ValidationResult()

        if(quote.isBlank()) {
            result = result.copy(quoteContentError = "Quote can't be empty.")
        } else if (quote.length < 10) {
            result = result.copy(quoteContentError = "Quote must be at least 10 characters long.")
        }

        return result
    }

    data class ValidationResult(
        val quoteContentError: String? = null,
    )
}


