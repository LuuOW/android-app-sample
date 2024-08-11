package com.lucaskempe.cropy.providers.websocket

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.lucaskempe.cropy.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import java.io.Serializable
import java.util.Base64
import javax.inject.Inject

class WebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    private val context: Context
) : WebSocketListener() {

    private var webSocketCli: WebSocket? = null

    fun connect() {
        val wss = BuildConfig.WSS_BASE_URL
        val request = Request.Builder().url("$wss/chat?userId=sqluser4").build()
        webSocketCli = okHttpClient.newWebSocket(request, this@WebSocketClient)
    }

    fun disconnect() {
        webSocketCli?.close(NORMAL_CLOSURE_STATUS, null)
        webSocketCli = null
    }

    fun sendMessage(senderId: String, recipientId: String, messageContent: String) {
        val message = Message(senderId, recipientId = recipientId, messageContent = messageContent)
        val json = gson.toJson(message)
        webSocketCli?.send(json)
    }

    fun sendEncodedMessage(base64Image: String, first: String, second: String, third: String) {
        val separator = ","
        val combinedString = "$base64Image$separator$first$separator$second$separator$third"
        webSocketCli?.send(bytes = combinedString.encodeUtf8())
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {

    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val message = gson.fromJson(text, Message::class.java)
        val intent = Intent("ACTION_MESSAGE_RECEIVED")
        intent.putExtra("MESSAGE", message.messageContent)
        context.sendBroadcast(intent)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        val image = Base64.getDecoder().decode(bytes.base64())
        val intent = Intent("ACTION_BASE64_MESSAGE_RECEIVED")
        intent.putExtra("BASE64_IMAGE", image)
        context.sendBroadcast(intent)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        val intent = Intent("ERROR")
        context.sendBroadcast(intent)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}


data class Message(
    val senderId: String,
    val recipientId: String,
    val messageContent: String
) : Serializable