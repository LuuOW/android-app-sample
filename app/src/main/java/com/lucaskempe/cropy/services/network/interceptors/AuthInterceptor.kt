package com.lucaskempe.cropy.services.network.interceptors

import android.content.Context
import com.lucaskempe.cropy.services.network.token.TokenUtil.retrieveToken
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking { retrieveToken(context) } // Run the suspend function in a blocking way

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}