package com.lucaskempe.cropy.services.network

import com.google.gson.GsonBuilder
import com.lucaskempe.cropy.services.network.api.BgErasingAPI
import com.lucaskempe.cropy.services.network.api.CodeVerifyAPI
import com.lucaskempe.cropy.services.network.api.ExchangeRateAPI
import com.lucaskempe.cropy.services.network.api.ImageUploadAPI
import com.lucaskempe.cropy.services.network.api.LoginAPI
import com.lucaskempe.cropy.services.network.api.MailRequestAPI
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

    val mailRequestAPI = retrofit.create(MailRequestAPI::class.java)

    val codeVerifyAPI = retrofit.create(CodeVerifyAPI::class.java)

    val loginAPI = retrofit.create(LoginAPI::class.java)

    val splashAPI = retrofit.create(SplashUIAPI::class.java)


}
