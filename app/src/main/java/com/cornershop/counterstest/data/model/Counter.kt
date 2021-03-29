package com.cornershop.counterstest.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Counter(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("count") val count: Int,
    @SerializedName("selected") var selected: Boolean
): Parcelable