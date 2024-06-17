package com.lucaskempe.cropy.services.workers

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.providers.data.UserSessionDataStore
import com.lucaskempe.cropy.services.models.request.CodeVerifyRequest
import com.lucaskempe.cropy.services.models.request.LoginRequest
import com.lucaskempe.cropy.services.models.response.LoginResponse
import com.lucaskempe.cropy.services.network.RetrofitClient
import com.lucaskempe.cropy.services.session.UserSession
import com.lucaskempe.cropy.services.workers.LoginWorker.Companion
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltWorker
class CodeVerifyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val newContext = context
    private val code = inputData.getString("code")!!

    private val outputData = mutableStateOf(workDataOf())

    companion object {
        const val baseUrl: String = BuildConfig.BASE_URL
    }


    override suspend fun doWork(): Result {
        try {
            val client = RetrofitClient(
                baseUrl)
            val call = client.codeVerifyAPI.verifyCode(CodeVerifyRequest(code))
            call.enqueue(object : Callback<CodeResponse> {
                override fun onResponse(
                    p0: Call<CodeResponse>,
                    p1: Response<CodeResponse>
                ) {
                    if (p1.isSuccessful) {
                        Log.d("CODE_VERIFY","status: ${p1.body()?.status}")
                        outputData.value = workDataOf("isValid" to true)
                    }
                }

                override fun onFailure(p0: Call<CodeResponse>, p1: Throwable) {
                    Log.d("Login", "${p1.message}, ${p1.cause}")
                }

            })
            delay(4000)
            return Result.success(outputData.value)

        } catch (e: Exception) {
            return Result.failure()
        }
    }

    data class CodeResponse(
        val status: String,
        val isValid: Boolean
    )


}