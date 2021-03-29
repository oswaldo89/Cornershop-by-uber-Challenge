package com.cornershop.counterstest.presentation.counters_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases

class VMFactory(private val repo: CounterUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CounterUseCases::class.java).newInstance(repo)
    }
}