package com.lucaskempe.cropy.services.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody

data class ExchangeRateResponse(
    val provider: String,
    val warning: String?,  // Optional field for upgrade warning
    val terms: String,
    val base: String,
    val date: String,
    @SerializedName("time_last_updated")
    val timeLastUpdated: Long,
    val rates: Map<String, Double>
)