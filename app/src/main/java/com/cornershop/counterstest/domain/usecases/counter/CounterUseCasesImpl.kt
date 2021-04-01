package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.data.repository.counter.CounterDataSourceImpl
import com.cornershop.counterstest.utils.Resource
import javax.inject.Inject

class CounterUseCasesImpl @Inject constructor(private val source: CounterDataSourceImpl): CounterUseCases{
    override suspend fun getList(): Resource<List<Counter>> {
        return source.getList()
    }

    override suspend fun addCounter(title : String): Resource<List<Counter>> {
        return source.addCounter(title)
    }

    override suspend fun deleteCounter(id : String): Resource<List<Counter>> {
        return source.deleteCounter(id)
    }

    override suspend fun increaseCounter(id : String): Resource<List<Counter>> {
        return source.increaseCounter(id)
    }

    override suspend fun decreaseCounter(id : String): Resource<List<Counter>> {
        return source.decreaseCounter(id)
    }
}

