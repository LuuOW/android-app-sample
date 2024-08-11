package com.cropy.network.interceptors

import android.content.Context
import com.cropy.datastore.user.UserSessionDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    private val token = MutableStateFlow("")

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        CoroutineScope(Dispatchers.IO+ Job()).launch {
            context.UserSessionDataStore.data.collect {
                if (it.token.isNotEmpty()) {
                    token.emit(it.token)
                }
            }
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${token.value}")
            .build()
        return chain.proceed(newRequest)
    }
}