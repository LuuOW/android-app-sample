package com.cropy.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkErrorConverter(
    private val context: Context,
    private val gson: Gson
) {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    suspend fun parseError(throwable: Throwable): Throwable {

        return if (throwable is retrofit2.HttpException) {
            val errorType = object : TypeToken<com.cropy.network.NetworkError>() {}.type
            try {
                val parsedError: com.cropy.network.NetworkError = gson.fromJson(throwable.response()?.errorBody()?.charStream(), errorType)
                val errorMessage = parsedError.detail ?: parsedError.data?.message ?: parsedError.title

                val finalError = if(errorMessage != null) {
                    when(throwable.code()) {
                        HttpURLConnection.HTTP_FORBIDDEN -> Throwable(errorMessage)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> Throwable(errorMessage)
                        HttpURLConnection.HTTP_NOT_FOUND -> Throwable(errorMessage)
                        else -> Throwable(errorMessage)
                    }
                } else {
                    throwable
                }
                finalError
            } catch (e: Throwable) {
                val message = when(throwable.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> "Please, try again later"
                    HttpURLConnection.HTTP_FORBIDDEN -> "Access problem"
                    HttpURLConnection.HTTP_UNAUTHORIZED -> "Invalid session, try again"
                    else -> "Please try again later"
                }
                val exception = Exception(message)
                exception
            }
        } else if (!isNetworkConnected()) {
            Throwable("Internet is not available.\\nPlease turn on mobile data or Wi-Fi")
        } else {
            when(throwable) {
                is ConnectException,
                    is SocketTimeoutException,
                    is UnknownHostException -> Throwable("No answer from server")
                else -> throwable
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val nw = connectivityManager?.activeNetwork ?: return false
        return connectivityManager.getNetworkCapabilities(nw)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

}