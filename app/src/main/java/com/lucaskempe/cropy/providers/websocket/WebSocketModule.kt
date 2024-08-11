package com.lucaskempe.cropy.providers.websocket

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WebSocketModule {

    @Provides
    fun provideWebSocketClient(
        okHttpClient: OkHttpClient,
        gson:Gson,
        @ApplicationContext context: Context
    ) = WebSocketClient(okHttpClient, gson, context)

}