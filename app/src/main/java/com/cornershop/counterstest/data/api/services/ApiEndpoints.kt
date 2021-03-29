package com.cornershop.counterstest.data.api.services

import com.cornershop.counterstest.data.model.Counter
import retrofit2.http.GET

interface ApiEndpoints {

    @GET("counters")
    suspend fun getList(): List<Counter>
}