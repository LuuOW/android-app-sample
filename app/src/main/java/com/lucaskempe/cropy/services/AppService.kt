package com.lucaskempe.cropy.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class AppService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fetchExchangeRates()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        fetchExchangeRates()
        return null
    }

    private fun fetchExchangeRates() {
        val workManager = WorkManager.getInstance(applicationContext)

        workManager.enqueueUniqueWork(
            "exchange_rate_fetch",
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequest.Builder(AppWorker::class.java).build()
        )
    }
}