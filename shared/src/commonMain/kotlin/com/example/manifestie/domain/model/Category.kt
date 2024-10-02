package com.example.manifestie.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long?,
    val title: String
)