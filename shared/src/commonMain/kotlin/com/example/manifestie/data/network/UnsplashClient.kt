package com.example.manifestie.data.network

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.Result
import com.example.manifestie.core.UNSPLASH_RANDOM_URL
import com.example.manifestie.core.unsplash_access_key
import com.example.manifestie.data.model.UnsplashResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class UnsplashClient(
    private val httpClient: HttpClient
) {

    suspend fun getRandomPhoto(): Result<UnsplashResponse, NetworkError> {
        val response = try {
            httpClient.get(urlString = UNSPLASH_RANDOM_URL) {
                parameter(
                    "client_id",
                    unsplash_access_key
                )
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when(response.status.value) {
            in 200..299 -> {
                val unsplashResponse = response.body<UnsplashResponse>()
                Result.Success(unsplashResponse)
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