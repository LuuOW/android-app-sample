package com.cropy.components.webview

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.cropy.components.topbar.AppTopBar

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(onBackPressed: () -> Unit = {}, title:String = "Default WebView") {
    val url = "https://google.com"

    Column(
        modifier = Modifier
            .systemBarsPadding()
    ) {
        AppTopBar(
            title = title,
            onLeadingIconPressed = onBackPressed,
        )


        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val uri = request?.url
                            Log.d("WebView", "Loading URL: $uri")
                            return super.shouldOverrideUrlLoading(view, request)
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            errorCode: Int,
                            description: String?,
                            failingUrl: String?
                        ) {
                            Log.e(
                                "WebView",
                                "Error: $errorCode, $description, URL: $failingUrl"
                            )
                        }
                    }
                    loadUrl(url)
                }
            },
            update = { webView ->
                webView.loadUrl(url)
            }
        )
    }
}