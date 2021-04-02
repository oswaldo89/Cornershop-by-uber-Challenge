package com.cornershop.counterstest.data.repository

/*
abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Exception) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(false, "", throwable)
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }
}*/