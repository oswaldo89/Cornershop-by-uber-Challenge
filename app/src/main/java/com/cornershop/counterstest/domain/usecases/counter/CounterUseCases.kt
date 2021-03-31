package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.utils.Resource

interface CounterUseCases{
    suspend fun getList() : Resource<List<Counter>>
    suspend fun addCounter(title : String) : Resource<List<Counter>>
    suspend fun deleteCounter(id : String) : Resource<List<Counter>>
    /*
   suspend fun increaseCounter() : Resource<List<Counter>>
   suspend fun decreaseCounter() : Resource<List<Counter>>*/
}