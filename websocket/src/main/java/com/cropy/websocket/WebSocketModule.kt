package com.cropy.websocket

import com.cropy.network.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WebSocketModule {

    private const val WSS_CLIENT_READ_TIMEOUT = 5L
    private const val WSS_CLIENT_WRITE_TIMEOUT = 5L
    private const val WSS_CLIENT_CONNECT_TIMEOUT = 5L

    @Provides
    @Singleton
    @Named("okHttpWss")
    fun providesOkHttpWssClient(authTokenInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(WSS_CLIENT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WSS_CLIENT_WRITE_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(WSS_CLIENT_READ_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(authTokenInterceptor)
        .build()

    @Provides
    @Singleton
    fun providesWebSocketService(@Named("okHttpWss") okHttpClient: OkHttpClient): WebSocketService = WebSocketService(okHttpClient)

}