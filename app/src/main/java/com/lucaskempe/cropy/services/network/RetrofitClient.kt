package com.lucaskempe.cropy.services.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.lucaskempe.cropy.services.network.api.BgErasingAPI
import com.lucaskempe.cropy.services.network.api.CodeVerifyAPI
import com.lucaskempe.cropy.services.network.api.ExchangeRateAPI
import com.lucaskempe.cropy.services.network.api.ImageUploadAPI
import com.lucaskempe.cropy.services.network.api.LoginAPI
import com.lucaskempe.cropy.services.network.api.MailRequestAPI
import com.lucaskempe.cropy.services.network.api.PasswordResetAPI
import com.lucaskempe.cropy.services.network.api.SMSRequestAPI
import com.lucaskempe.cropy.services.network.api.SignUpAPI
import com.lucaskempe.cropy.services.network.api.SplashUIAPI
import com.lucaskempe.cropy.services.network.interceptors.AuthInterceptor
import com.lucaskempe.cropy.services.network.token.TokenUtil.retrieveToken
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitClient(private val baseURL: String, private val context: Context) {

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // Add the interceptor
            .build()

        Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient) // Use the configured OkHttp client
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val imageAPI = retrofit.create(ImageUploadAPI::class.java)

    val mailRequestAPI = retrofit.create(MailRequestAPI::class.java)
    val passwordResetAPI = retrofit.create(PasswordResetAPI::class.java)
    val signupAPI = retrofit.create(SignUpAPI::class.java)

    val smsRequestAPI = retrofit.create(SMSRequestAPI::class.java)

    val codeVerifyAPI = retrofit.create(CodeVerifyAPI::class.java)

    val loginAPI = retrofit.create(LoginAPI::class.java)

    val splashAPI = retrofit.create(SplashUIAPI::class.java)


}
