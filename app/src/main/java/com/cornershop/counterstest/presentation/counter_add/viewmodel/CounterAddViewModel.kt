package com.cornershop.counterstest.presentation.counter_add.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CounterAddViewModel @Inject constructor(private val repo: CounterUseCases) : ViewModel() {
    private val counterName = MutableLiveData<String>()

    fun addCounter(name: String){
        this.counterName.value = name
    }

    val addCounterObservable   = counterName.switchMap { name ->
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                delay(TimeUnit.SECONDS.toMillis(1)) //simulate slow server.
                emit(repo.addCounter(name))
            } catch (e: Exception) {
                emit(Resource.Failure(e.message ?: "Unknown Error", e))
            }
        }
    }
}