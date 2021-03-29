package com.cornershop.counterstest.data.model


import com.google.gson.annotations.SerializedName

data class Counter(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("count") val count: Int
)