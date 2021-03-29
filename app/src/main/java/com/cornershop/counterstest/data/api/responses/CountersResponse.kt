package com.cornershop.counterstest.data.api.responses

import com.cornershop.counterstest.data.model.Counter
import com.google.gson.annotations.SerializedName

data class CountersResponse(
    @SerializedName("counters") val counters: List<Counter>
)