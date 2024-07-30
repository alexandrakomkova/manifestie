package com.example.manifestie

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform