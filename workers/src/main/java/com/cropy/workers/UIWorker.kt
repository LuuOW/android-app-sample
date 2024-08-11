package com.cropy.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.cropy.datastore.ui.UIDataStore
import com.cropy.repositories.api.UIRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UIWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val uiRepository: UIRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val splashReq = uiRepository.fetchAllUI()
        val splashData = splashReq.getOrNull()
        if (splashReq.isSuccess && splashData != null) {
            context.UIDataStore.updateData { splashData }
            return Result.success(workDataOf("splash" to "success"))
        } else {
            return Result.failure()
        }
    }


}