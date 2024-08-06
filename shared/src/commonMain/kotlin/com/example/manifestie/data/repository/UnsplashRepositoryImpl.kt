package com.example.manifestie.data.repository

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.Result
import com.example.manifestie.core.map
import com.example.manifestie.data.network.UnsplashClient
import com.example.manifestie.domain.repository.UnsplashRepository

class UnsplashRepositoryImpl(
    private val unsplashClient: UnsplashClient
): UnsplashRepository {
    override suspend fun getRandomPhoto(): Result<String?, NetworkError> =
        unsplashClient.getRandomPhoto().map { it.urls.regular }

}