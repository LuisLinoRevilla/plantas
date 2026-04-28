package com.example.plantas.core

sealed class ResponseService {
    data class Success<T>(val data: T): ResponseService<T>()
    data class Error(val error: String)
    object Loading: ResponseService<Nothig>()
}