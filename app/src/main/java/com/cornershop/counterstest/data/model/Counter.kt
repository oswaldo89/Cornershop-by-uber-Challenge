package com.cornershop.counterstest.data.model


import com.google.gson.annotations.SerializedName

data class Counter(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("count") val count: Int,
)