package com.lucaskempe.cropy.services.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val baseUrl = "https://api.exchangerate-api.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // Use GSON for JSON conversion
            .build()
    }

    fun getExchangeRateApi(): ExchangeRateAPI {
        return if (retrofit != null) {
            retrofit.create(ExchangeRateAPI::class.java)
        } else {
            throw IllegalStateException("Retrofit not initialized")
        }
    }
}
