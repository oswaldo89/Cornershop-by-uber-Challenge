package com.cornershop.counterstest.presentation.counters_list.adapter

import com.cornershop.counterstest.data.model.Counter

interface ICounterList{
    fun onItemClick(item: Counter, position: Int)
    fun onItemLongClick(item: Counter, position: Int)

    fun onIncreaseClick(item: Counter)
    fun onDecreaseClick(item: Counter)
}