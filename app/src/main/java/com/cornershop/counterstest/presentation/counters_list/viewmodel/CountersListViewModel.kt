package com.cornershop.counterstest.presentation.counters_list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.utils.Constants
import com.cornershop.counterstest.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CountersListViewModel @Inject constructor(private val repo: CounterUseCases) : ViewModel() {



    private val mutableCounterList = MutableLiveData<ArrayList<Counter>>()
    private val mutableCountersDeletedList = MutableLiveData<ArrayList<Counter>>()
    private val mutableCounter = MutableLiveData<Counter>()
    private lateinit var modificationType : String

    fun deleteCountersList(list: ArrayList<Counter>){
        mutableCountersDeletedList.value = list
    }

    fun changeCountersList(list: ArrayList<Counter>){
        mutableCounterList.value = list
    }

    fun modificationCounter(counter: Counter, changeType: String){
        mutableCounter.value = counter
        this.modificationType = changeType
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

    val observeDeletedItems   = mutableCountersDeletedList.switchMap { list ->
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {

                list.mapIndexed { index, counter ->
                    repo.deleteCounter(counter.id)
                    if(list.size == index + 1)  emit(repo.deleteCounter(counter.id))
                }

            } catch (e: Exception) {
                emit(Resource.Failure(e.message ?: "Unknown Error", e))
            }
        }
    }

    val observeCounterModification   = mutableCounter.switchMap { counter ->
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                when(modificationType){
                    Constants.INCREASE -> emit(repo.increaseCounter(counter.id))
                    Constants.DECREASE -> emit(repo.decreaseCounter(counter.id))
                }
            } catch (e: Exception) {
                emit(Resource.Failure(e.message ?: "Unknown Error", e))
            }
        }
    }
}