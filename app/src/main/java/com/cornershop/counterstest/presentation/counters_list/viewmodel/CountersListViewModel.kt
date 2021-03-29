package com.cornershop.counterstest.presentation.counters_list.viewmodel

import androidx.lifecycle.*
import com.cornershop.counterstest.data.api.responses.CountersResponse
import com.cornershop.counterstest.data.repository.counter.CounterDataSourceImpl
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.utils.Resource
import kotlinx.coroutines.Dispatchers


class CountersListViewModel(private val repo: CounterUseCases) : ViewModel() {

    val counterList   = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(repo.getList())
        } catch (e: Exception) {
            emit(Resource.Failure(e.message ?: "Unknown Error", e))
        }
    }

}