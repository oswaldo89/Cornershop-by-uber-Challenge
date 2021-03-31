package com.cornershop.counterstest.data.api.request


import com.google.gson.annotations.SerializedName

data class IdCounterRequest(
    @SerializedName("id") val title: String
)