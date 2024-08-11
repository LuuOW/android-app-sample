package com.cropy.maps.network

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MapsAuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalReq = chain.request()
        //val token = BuildConfig.MAPS_API_KEY
        val newRequest = originalReq.newBuilder()
            //.header("X-Goog-Api-Key:", value = token)
            .build()
        return chain.proceed(newRequest)
    }

}