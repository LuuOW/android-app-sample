package com.lucaskempe.cropy.services.network

import com.google.gson.GsonBuilder
import com.lucaskempe.cropy.services.network.api.BgErasingAPI
import com.lucaskempe.cropy.services.network.api.ExchangeRateAPI
import com.lucaskempe.cropy.services.network.api.ImageUploadAPI
import com.lucaskempe.cropy.services.network.api.LoginAPI
import com.lucaskempe.cropy.services.network.api.SplashUIAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(private val baseURL: String) {

    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Use GSON for JSON conversion
            .build()
    }

    val imageAPI = retrofit.create(ImageUploadAPI::class.java)

    val loginAPI = retrofit.create(LoginAPI::class.java)

    val splashAPI = retrofit.create(SplashUIAPI::class.java)

    val bgEraserAPI = retrofit.create(BgErasingAPI::class.java)

    fun getExchangeRateApi(): ExchangeRateAPI {
        return if (retrofit != null) {
            retrofit.create(ExchangeRateAPI::class.java)
        } else {
            throw IllegalStateException("Retrofit not initialized")
        }
    }

}
