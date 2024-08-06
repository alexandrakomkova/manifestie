package com.example.manifestie.core

enum class NetworkError(val errorDescription: String): Error {
    REQUEST_TIMEOUT("Response time exceeded."),
    UNAUTHORIZED("Authentication error occurred."),
    CONFLICT("Request conflicts with the current state of the server."),
    TOO_MANY_REQUESTS("Too many requests."),
    NO_INTERNET("Check your Internet connection"),
    PAYLOAD_TOO_LARGE("Request entity is larger than limits defined by server."),
    SERVER_ERROR("Server problem occurred."),
    SERIALIZATION("Serialization problem occurred."),
    UNKNOWN("Unknown error occurred.")
}

