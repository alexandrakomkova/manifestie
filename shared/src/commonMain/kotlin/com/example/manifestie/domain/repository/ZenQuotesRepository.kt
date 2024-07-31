package com.example.manifestie.domain.repository

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.Result

interface ZenQuotesRepository {
    suspend fun getRandomQuote(): Result<String?, NetworkError>
}