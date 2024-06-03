package com.lucaskempe.cropy.providers.websocket

import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import kotlinx.coroutines.*
import okhttp3.*
import java.io.Serializable

class WebSocketClient(
    private val urlString: String,
    private val onMessageReceived: (Message) -> Unit,
    private val onConnectionOpened: () -> Unit,
    private val onConnectionClosed: (Int, String?) -> Unit,
    private val onConnectionFailed: (Throwable) -> Unit,
    private val vm : ChatViewModel
) : WebSocketListener() {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    fun connect() {
        val request = Request.Builder().url(urlString).build()
        webSocket = client.newWebSocket(request, this)
    }

    fun disconnect() {
        webSocket?.close(NORMAL_CLOSURE_STATUS, null)
        webSocket = null
    }

    fun sendMessage(recipientId: String, messageContent: String) {
        val message = Message(recipientId = recipientId, messageContent = messageContent)
        vm.messages.add(com.lucaskempe.cropy.ui.screens.Message(messageContent,isFromCurrentUser = true,recipientId ))
        val json = gson.toJson(message)
        webSocket?.send(json)
    }

    // WebSocketListener Overrides
    override fun onOpen(webSocket: WebSocket, response: Response) {
        onConnectionOpened()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        CoroutineScope(Dispatchers.Main).launch { // Handle UI updates on the main thread
            try {
                val message = gson.fromJson(text, Message::class.java)
                onMessageReceived(message)
            } catch (e: Exception) {
                println("Error parsing message: $text")
            }
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        onConnectionClosed(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        onConnectionFailed(t)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}

data class Message(
    val recipientId: String,
    val messageContent: String
): Serializable