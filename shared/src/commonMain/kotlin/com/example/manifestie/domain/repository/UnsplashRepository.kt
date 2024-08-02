package com.example.manifestie.domain.repository

import com.example.manifestie.core.NetworkError
import com.example.manifestie.core.Result

interface UnsplashRepository {
    suspend fun getRandomPhoto(): Result<String?, NetworkError>
}