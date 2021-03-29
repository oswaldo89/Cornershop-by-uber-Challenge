package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.data.repository.counter.CounterDataSourceImpl
import com.cornershop.counterstest.utils.Resource

class CounterUseCasesImpl(private val source: CounterDataSourceImpl): CounterUseCases{
    override suspend fun getList(): Resource<List<Counter>> {
        return source.getList()
    }
}

