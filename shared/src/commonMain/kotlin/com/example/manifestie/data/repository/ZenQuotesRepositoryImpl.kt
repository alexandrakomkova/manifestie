package com.example.manifestie.data.repository

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.Result
import com.example.manifestie.core.map
import com.example.manifestie.data.network.ZenQuotesClient
import com.example.manifestie.domain.repository.ZenQuotesRepository

class ZenQuotesRepositoryImpl (
    private val zenQuotesClient: ZenQuotesClient
): ZenQuotesRepository {
    override suspend fun getRandomQuote(): Result<String?, NetworkError> =
        zenQuotesClient.getRandomQuote().map { it[0].q }
}