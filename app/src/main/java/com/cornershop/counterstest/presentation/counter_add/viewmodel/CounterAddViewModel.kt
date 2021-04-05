package com.cornershop.counterstest.presentation.counter_add.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.utils.sealed_classes.MainListUiState
import com.cornershop.counterstest.utils.sealed_classes.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.net.UnknownHostException
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
            try {
                emit(repo.addCounter(name))
            }catch (e: Throwable) {
                delay(TimeUnit.SECONDS.toMillis(1))
                when (e) {
                    is UnknownHostException -> {
                        emit(Resource.NetworkError(e.message ?: "Unknown Error", e))
                    }
                    else -> {
                        if(e.message == "HTTP 404 Not Found"){
                            emit(Resource.NetworkError(e.message ?: "Unknown Error", e))
                        }else{
                            emit(Resource.Failure(e.message ?: "Unknown Error", e))
                        }
                    }
                }
            }
        }
    }
}