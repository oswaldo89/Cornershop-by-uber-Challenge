package com.cornershop.counterstest.data.api.services

import com.cornershop.counterstest.data.api.request.AddCounterRequest
import com.cornershop.counterstest.data.model.Counter
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndpoints {

    @GET("counters")
    suspend fun getList(): List<Counter>

    @POST("counter")
    suspend fun addCounter(@Body body: AddCounterRequest): List<Counter>
}