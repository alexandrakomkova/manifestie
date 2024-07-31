package com.example.manifestie.data

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.Result
import com.example.manifestie.domain.repository.ZenQuotesRepository
import com.example.manifestie.network.ZenQuotesClient

class ZenQuotesRepositoryImpl (
    private val zenQuotesClient: ZenQuotesClient
): ZenQuotesRepository {
    override suspend fun getRandomQuote(): Result<String?, NetworkError> =
        zenQuotesClient.getRandomQuote()
}