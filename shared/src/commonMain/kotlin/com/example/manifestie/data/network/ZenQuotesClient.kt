package com.example.manifestie.data.network

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.Result
import com.example.manifestie.core.ZEN_QUOTES_RANDOM_URL
import com.example.manifestie.data.ZenQuotes
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class ZenQuotesClient (
    private val httpClient: HttpClient
) {

    suspend fun getRandomQuote(): Result<Array<ZenQuotes>, NetworkError> {
        val response = try {
            httpClient.get(urlString = ZEN_QUOTES_RANDOM_URL)
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {
            in 200..299 -> {
                val zenQuotesResponse = response.body<Array<ZenQuotes>>()
                //Result.Success(zenQuotesResponse[0].q)
                Result.Success(zenQuotesResponse)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}