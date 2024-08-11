package com.cropy.workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cropy.repositories.api.ImageUploadRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.io.FileOutputStream

@HiltWorker
class ImageUploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val imageUploadRepository: ImageUploadRepository
) : CoroutineWorker(context, workerParams) {

    private val image = inputData.getString("image")!!
    private val filename = inputData.getString("filename")!!
    private val userId = inputData.getString("userId")!!

    override suspend fun doWork(): Result {
        val imageUri = Uri.parse(image)
        val result = imageUploadRepository.uploadImage(imageUri, filename, userId)
        if (result.isSuccess && result.getOrNull() != null) {
            return Result.success()
        } else {
            return Result.failure()
        }
    }

}