package com.cornershop.counterstest.data.repository.counter

import com.cornershop.counterstest.data.api.RetrofitClient
import com.cornershop.counterstest.data.api.request.AddCounterRequest
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.utils.Resource

class CounterDataSourceImpl : CounterUseCases {
    override suspend fun getList(): Resource<List<Counter>> {
        return Resource.Success(RetrofitClient.webservice.getList())
    }

    override suspend fun addCounter(name: String): Resource<List<Counter>> {
        return Resource.Success(RetrofitClient.webservice.addCounter(AddCounterRequest(name)))
    }

    /*override suspend fun deleteCounter(): Resource<CountersResponse> {

    }

    override suspend fun increaseCounter(): Resource<CountersResponse> {

    }

    override suspend fun decreaseCounter(): Resource<CountersResponse> {

    }*/


}