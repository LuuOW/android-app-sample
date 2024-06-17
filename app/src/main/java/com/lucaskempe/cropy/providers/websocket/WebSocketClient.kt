package com.lucaskempe.cropy.providers.websocket

import android.content.Context
import android.view.View
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.R
import com.lucaskempe.cropy.ui.screens.chat.models.MessageModel
import com.lucaskempe.cropy.ui.screens.chat.utils.ChatUtils.formatTimestampToHourMinute
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import kotlinx.coroutines.*
import okhttp3.*
import java.io.Serializable
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class WebSocketClient(
    private val state: SnackbarHostState,
    private val urlString: String,
    private val onMessageReceived: (Message) -> Unit = {},
    private val onConnectionOpened: () -> Unit = {},
    private val onConnectionClosed: (Int, String?) -> Unit = { _, _ -> },
    private val onConnectionFailed: (Throwable) -> Unit = {},
    private val vm : ChatViewModel,
    private val view: View
) : WebSocketListener() {

    private val client = OkHttpClient()
    private var webSocketCli: WebSocket? = null
    private val gson = Gson()

    fun connect(context: Context) {
        try {
            val wss = BuildConfig.WSS_BASE_URL
            val request = Request.Builder()
                .addHeader("Authorization", "Bearer ")
                .url("$wss/chat?userId=664e8c99a2f3854f5a62caf7").build()
            webSocketCli = client.newWebSocket(request, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun disconnect() {
        webSocketCli?.close(NORMAL_CLOSURE_STATUS, null)
        webSocketCli = null
    }

    fun sendMessage(senderId: String ,recipientId: String, messageContent: String) {
        val message = Message(senderId, recipientId = recipientId, messageContent = messageContent)
        vm.messages.add(
            MessageModel(
                messageContent,
                isFromCurrentUser = true,
                recipientId
            )
        )
        val json = gson.toJson(message)
        webSocketCli?.send(json)
    }

    // WebSocketListener Overrides
    override fun onOpen(webSocket: WebSocket, response: Response) {
        onConnectionOpened()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        CoroutineScope(Dispatchers.Main).launch { // Handle UI updates on the main thread
            try {
                val message = gson.fromJson(text, Message::class.java)
                vm.messages.add(MessageModel(message.messageContent, isFromCurrentUser = false, timestamp = formatTimestampToHourMinute()))
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
        t.printStackTrace()
        println(response?.message)
        CoroutineScope(Dispatchers.IO+ Job()).launch {
            Snackbar
                .make(view, "WSS Connection Failed", Snackbar.LENGTH_SHORT)
                .setAction("Retry") {
                    val request = Request.Builder().url(urlString).build()
                    webSocketCli = client.newWebSocket(request, this@WebSocketClient)
                }
                .setActionTextColor(ContextCompat.getColor(vm.context, R.color.white))
                    .setBackgroundTint(ContextCompat.getColor(vm.context, R.color.notred))
                    .show()

/*            val result = state.showSnackbar(
                "WSS Connection Failed",
                "Retry",
                SnackbarDuration.Short

                if (result == SnackbarResult.ActionPerformed) {
                val request = Request.Builder().url(urlString).build()
                webSocketCli = client.newWebSocket(request, this@WebSocketClient)
            }

            )*/
        }
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    private fun getTrustManager(context: Context): X509TrustManager? {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val inputStream = context.resources.openRawResource(R.raw.cert)
            val certificate = certificateFactory.generateCertificate(inputStream) as X509Certificate
            inputStream.close()

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType).apply {
                load(null, null)
                setCertificateEntry("ca", certificate)
            }

            println("Certificate Subject: ${certificate.subjectDN.name}")
            println("Certificate Issuer: ${certificate.issuerDN.name}")
            println("Certificate Valid From: ${certificate.notBefore}")
            println("Certificate Valid To: ${certificate.notAfter}")


            val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            val trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
            trustManagerFactory.init(keyStore)
            return trustManagerFactory.trustManagers[0] as X509TrustManager
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}



data class Message(
    val senderId: String,
    val recipientId: String,
    val messageContent: String
): Serializable