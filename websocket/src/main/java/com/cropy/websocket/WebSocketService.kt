package com.cropy.websocket

import okhttp3.OkHttpClient
import okhttp3.WebSocketListener
import javax.inject.Inject

class WebSocketService @Inject constructor(
    private val okHttpClient: OkHttpClient,
): WebSocketListener() {




}