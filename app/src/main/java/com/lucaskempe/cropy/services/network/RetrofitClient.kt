package com.lucaskempe.cropy.services.network

import com.google.gson.GsonBuilder
import com.lucaskempe.cropy.services.network.api.ExchangeRateAPI
import com.lucaskempe.cropy.services.network.api.ImageUploadAPI
import com.lucaskempe.cropy.services.network.api.LoginAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://8081-monospace-cropyback-1715559709578.cluster-m7tpz3bmgjgoqrktlvd4ykrc2m.cloudworkstations.dev"
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Use GSON for JSON conversion
            .build()
    }

    val imageAPI = retrofit.create(ImageUploadAPI::class.java)

    val loginAPI = retrofit.create(LoginAPI::class.java)

    fun getExchangeRateApi(): ExchangeRateAPI {
        return if (retrofit != null) {
            retrofit.create(ExchangeRateAPI::class.java)
        } else {
            throw IllegalStateException("Retrofit not initialized")
        }
    }

}
