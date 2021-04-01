package com.cornershop.counterstest.di

import com.cornershop.counterstest.data.repository.counter.CounterDataSource
import com.cornershop.counterstest.data.repository.counter.CounterDataSourceImpl
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCases
import com.cornershop.counterstest.domain.usecases.counter.CounterUseCasesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityModule {

    @Binds
    abstract fun counterUseCasesImpl(useCasesImpl: CounterUseCasesImpl) : CounterUseCases

    @Binds
    abstract fun counterDataSourceImpl(counterDataSourceImpl: CounterDataSourceImpl) : CounterDataSource
}