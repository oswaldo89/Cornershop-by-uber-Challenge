package com.cornershop.counterstest.data.repository.counter

import com.cornershop.counterstest.data.api.RetrofitClient
import com.cornershop.counterstest.data.api.request.IdCounterRequest
import com.cornershop.counterstest.data.api.request.TitleCounterRequest
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.utils.Resource

class CounterDataSourceImpl : CounterUseCases {
    override suspend fun getList(): Resource<List<Counter>> {
        return Resource.Success(RetrofitClient.webservice.getList())
    }

    override suspend fun addCounter(title : String): Resource<List<Counter>> {
        return Resource.Success(RetrofitClient.webservice.addCounter(TitleCounterRequest(title)))
    }

    override suspend fun deleteCounter(id : String):  Resource<List<Counter>> {
        return Resource.Success(RetrofitClient.webservice.deleteCounter(IdCounterRequest(id)))
    }

    /*

    override suspend fun increaseCounter(): Resource<CountersResponse> {

    }

    override suspend fun decreaseCounter(): Resource<CountersResponse> {

    }*/


}