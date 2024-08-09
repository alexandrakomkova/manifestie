package com.example.manifestie.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ZenQuotes (
    val q: String? = null,
    val a: String? = null,
    val h: String? = null
)