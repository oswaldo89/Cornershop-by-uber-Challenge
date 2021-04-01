package com.cornershop.counterstest.utils

sealed class Resource<out T> {
    class Loading<out T>: Resource<T>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Failure(val errorMessage: String?, val error: Throwable?): Resource<Nothing>()
    data class NetworkError(val errorMessage: String?, val error: Throwable?): Resource<Nothing>()
}