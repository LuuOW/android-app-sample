package com.lucaskempe.cropy.providers.websocket

import android.content.Context
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.lucaskempe.cropy.R
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

    fun connect(context: Context) {
        val trustManager = getTrustManager(context) as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)

        // Create a custom OkHttpClient with the SSL context
        val client = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true } // Only for self-signed, unsafe in production!
            .build()

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
    val recipientId: String,
    val messageContent: String
): Serializable