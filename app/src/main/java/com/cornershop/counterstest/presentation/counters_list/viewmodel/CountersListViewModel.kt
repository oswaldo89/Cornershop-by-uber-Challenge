package com.cornershop.counterstest.presentation.counters_list.viewmodel

import androidx.lifecycle.*
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.utils.Resource
import kotlinx.coroutines.Dispatchers


class CountersListViewModel(private val repo: CounterUseCases) : ViewModel() {

    private val mutableCounterList = MutableLiveData<ArrayList<Counter>>()

    fun changeCountersList(list: ArrayList<Counter>){
        mutableCounterList.value = list
    }

    val counterList   = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(repo.getList())
        } catch (e: Exception) {
            emit(Resource.Failure(e.message ?: "Unknown Error", e))
        }
    }

    val counterListChange = mutableCounterList.switchMap { list ->
        liveData(Dispatchers.IO) {
            emit(Resource.Success(list))
        }
    }
}