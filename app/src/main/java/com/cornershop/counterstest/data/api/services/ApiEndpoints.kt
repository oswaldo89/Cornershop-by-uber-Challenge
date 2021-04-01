package com.cornershop.counterstest.data.api.services

import com.cornershop.counterstest.data.api.request.IdCounterRequest
import com.cornershop.counterstest.data.api.request.TitleCounterRequest
import com.cornershop.counterstest.data.model.Counter
import retrofit2.http.*

interface ApiEndpoints {

    @GET("counters")
    suspend fun getList(): List<Counter>

    @POST("counter")
    suspend fun addCounter(@Body body: TitleCounterRequest): List<Counter>

    @HTTP(method = "DELETE", path = "counter", hasBody = true)
    suspend fun deleteCounter(@Body body: IdCounterRequest): List<Counter>

    @POST("counter/inc")
    suspend fun incCounter(@Body body: IdCounterRequest): List<Counter>

    @POST("counter/dec")
    suspend fun decCounter(@Body body: IdCounterRequest): List<Counter>
}