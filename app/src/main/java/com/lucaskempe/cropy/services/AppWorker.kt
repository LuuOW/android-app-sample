package com.lucaskempe.cropy.services

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.services.models.ExchangeRateResponse
import com.lucaskempe.cropy.services.models.User
import com.lucaskempe.cropy.services.network.RetrofitClient
import com.lucaskempe.cropy.services.repository.LocalDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val notificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val baseCurrency = inputData.getString("usd")
    private val retrofitClient = RetrofitClient.getExchangeRateApi()

    private val localDataSource = LocalDataSourceImpl(context)

    override fun doWork(): Result {


        CoroutineScope(Dispatchers.IO + Job()).launch {
            val response = retrofitClient.getExchangeRate(baseCurrency ?: "usd")
            if (response.isSuccessful) {
                val exchangeRateData = response.body() as ExchangeRateResponse
                // Access the parsed data from exchangeRateData object
                val usdToArsRate = exchangeRateData.rates["ARS"] ?: 0.0
                localDataSource.saveLocalData(exchangeRateData)
                localDataSource.getLocalData().base
                Log.d("response", usdToArsRate.toString())
            } else {
                throw IllegalArgumentException("Name cannot be blank")
            }
        }
        
        return Result.success() // Assuming success after waiting for the coroutine
    }

    private fun showNotification() {
        // ... Notification logic using results ...
        notificationManager.notify(2, Notification())
    }
}
