package com.cornershop.counterstest.presentation.counters_list.viewmodel

import androidx.lifecycle.*
import com.cornershop.counterstest.data.model.Counter
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.utils.Constants
import com.cornershop.counterstest.utils.sealed_classes.MainListUiState
import com.cornershop.counterstest.utils.sealed_classes.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CountersListViewModel @Inject constructor(private val repo: CounterUseCases) : ViewModel() {


    private val _mainListUiState = MutableStateFlow<MainListUiState>(MainListUiState.Initial)
    val mainListUiState : StateFlow<MainListUiState> = _mainListUiState
    //val hasOrNotContent = MutableStateFlow(false)

    private val mutableCounterList = MutableLiveData<ArrayList<Counter>>()
    private val loadTrigger = MutableLiveData(Unit)
    private val mutableCountersDeletedList = MutableLiveData<ArrayList<Counter>>()
    private val mutableCounter = MutableLiveData<Counter>()
    private lateinit var modificationType : String

    init {
        _mainListUiState.value = MainListUiState.Initial
    }

    fun attemptGetData() {
        loadTrigger.value = Unit
    }

    fun hasOrNotContent(hasContent : Boolean){
        _mainListUiState.value = if(hasContent) MainListUiState.HasContent else MainListUiState.NoContent
    }

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


    val counterList   = loadTrigger.switchMap {
        liveData(Dispatchers.IO) {
            _mainListUiState.value = MainListUiState.Loading
            try {
                emit(repo.getList())
            } catch (e: Throwable) {
                delay(TimeUnit.SECONDS.toMillis(1))
                when (e) {
                    is UnknownHostException -> {
                        emit(Resource.NetworkError(e.message ?: "Unknown Error", e))
                    }
                    else -> {
                        emit(Resource.Failure(e.message ?: "Unknown Error", e))
                        _mainListUiState.value = MainListUiState.Error(e.message ?: "Unknown Error")
                    }
                }
            }
        }
    }

    val counterListChange = mutableCounterList.switchMap { list ->
        liveData(Dispatchers.IO) {
            emit(Resource.Success(list))
            _mainListUiState.value = MainListUiState.HasContent
        }
    }

    val observeDeletedItems   = mutableCountersDeletedList.switchMap { list ->
        liveData(Dispatchers.IO) {
            _mainListUiState.value = MainListUiState.Loading
            try {

                list.mapIndexed { index, counter ->
                    repo.deleteCounter(counter.id)
                    if(list.size == index + 1)  emit(repo.deleteCounter(counter.id))
                }
                _mainListUiState.value = MainListUiState.HasContent

            } catch (e: Throwable) {
                delay(TimeUnit.SECONDS.toMillis(1))
                when (e) {
                    is UnknownHostException -> {
                        emit(Resource.NetworkError(e.message ?: "Unknown Error", e))
                    }
                    else -> {
                        emit(Resource.Failure(e.message ?: "Unknown Error", e))
                        _mainListUiState.value = MainListUiState.Error(e.message ?: "Unknown Error")
                    }
                }
            }
        }
    }

    val observeCounterModification   = mutableCounter.switchMap { counter ->
        liveData(Dispatchers.IO) {
            _mainListUiState.value = MainListUiState.Loading
            try {
                when(modificationType){
                    Constants.INCREASE -> emit(repo.increaseCounter(counter.id))
                    Constants.DECREASE -> emit(repo.decreaseCounter(counter.id))
                }
                _mainListUiState.value = MainListUiState.HasContent
            } catch (e: Throwable) {
                delay(TimeUnit.SECONDS.toMillis(1))
                when (e) {
                    is UnknownHostException -> {
                        emit(Resource.NetworkError(e.message ?: "Unknown Error", e))
                    }
                    else -> {
                        emit(Resource.Failure(e.message ?: "Unknown Error", e))
                        _mainListUiState.value = MainListUiState.Error(e.message ?: "Unknown Error")
                    }
                }
            }
        }
    }
}