package com.cornershop.counterstest.data.api.request


import com.google.gson.annotations.SerializedName

data class TitleCounterRequest(
    @SerializedName("title") val title: String
)