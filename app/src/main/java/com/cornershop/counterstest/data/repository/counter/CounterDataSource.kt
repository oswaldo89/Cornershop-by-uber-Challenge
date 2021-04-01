package com.cornershop.counterstest.data.repository.counter

import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.utils.Resource

interface CounterDataSource {
    suspend fun getList(): Resource<List<Counter>>
    suspend fun addCounter(title : String): Resource<List<Counter>>
    suspend fun deleteCounter(id : String): Resource<List<Counter>>
}