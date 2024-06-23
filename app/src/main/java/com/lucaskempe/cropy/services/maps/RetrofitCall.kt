package com.lucaskempe.cropy.services.maps

import android.content.Context
import com.google.gson.GsonBuilder
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.network.interceptors.AuthInterceptor
import com.lucaskempe.cropy.services.network.token.TokenUtil.retrieveToken
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitCall(context: Context) {

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val baseUrl = "https://maps.googleapis.com"

    private val retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
           // .addInterceptor(MapsAuthInterceptor(context)) // Add the interceptor
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient) // Use the configured OkHttp client
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val directionsAPI = retrofit.create(DirectionsAPI::class.java)

}

class MapsAuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking { BuildConfig.DIRECTIONS_KEY } // Run the suspend function in a blocking way

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}