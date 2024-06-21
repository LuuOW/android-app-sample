package com.lucaskempe.cropy.services.workers

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.request.PasswordResetRequest
import com.lucaskempe.cropy.services.network.RetrofitClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordResetWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val newContext = context

    companion object {
        const val baseUrl: String = BuildConfig.BASE_URL
    }

    private val phone = inputData.getString("phone")!!
    private val newPassword = inputData.getString("password")!!
    private val outputData = mutableStateOf(workDataOf())

    override suspend fun doWork(): Result {
        try {
            val client = RetrofitClient(baseUrl,newContext).passwordResetAPI
            val call = client.resetPassword(PasswordResetRequest(phone, newPassword))
            call.enqueue(object: Callback<String> {
                override fun onResponse(p0: Call<String>, p1: Response<String>) {

                }

                override fun onFailure(p0: Call<String>, p1: Throwable) {

                }
            })
            delay(5000)
            return Result.success(outputData.value)
        } catch (e: Exception) {
            return Result.failure()
        }
    }

}