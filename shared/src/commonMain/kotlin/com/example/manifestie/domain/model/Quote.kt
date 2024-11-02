package com.example.manifestie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val id: String = "",
    val quote: String
)
