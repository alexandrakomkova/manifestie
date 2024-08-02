package com.example.manifestie.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashResponse (
    val urls: UnsplashPhotoUrls
)

@Serializable
data class UnsplashPhotoUrls (
    val small: String
)