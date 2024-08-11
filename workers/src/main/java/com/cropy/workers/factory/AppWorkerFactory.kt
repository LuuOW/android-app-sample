package com.cropy.workers.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.cropy.workers.LocationWorker
import com.cropy.workers.NotificationsWorker
import com.cropy.workers.UIWorker
import com.cropy.repositories.api.AuthRepository
import com.cropy.repositories.api.ImageUploadRepository
import com.cropy.repositories.api.UIRepository
import com.cropy.workers.ImageUploadWorker
import javax.inject.Inject

class AppWorkerFactory @Inject constructor(
    private val authRepository: AuthRepository,
    private val uiRepository: UIRepository,
    private val imageRepository: ImageUploadRepository
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            ImageUploadWorker::class.java.name -> ImageUploadWorker(appContext,workerParameters, imageRepository)
            UIWorker::class.java.name -> UIWorker(appContext, workerParameters, uiRepository)
            LocationWorker::class.java.name -> LocationWorker(appContext, workerParameters)
            NotificationsWorker::class.java.name -> NotificationsWorker(appContext, workerParameters)
            else -> null
        }
    }
}