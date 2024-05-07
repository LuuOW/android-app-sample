package com.lucaskempe.cropy.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf

class AppService : LifecycleService() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val imageUri = intent?.getStringExtra("imageUri")
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniqueWork(
            "one_time_work",
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequest.Builder(AppWorker::class.java)
                .setInputData( workDataOf("imageUri" to imageUri))
                .build()
        )
        return super.onStartCommand(intent, flags, startId)
    }

    private fun launchOneTimeWork() {

    }
}
