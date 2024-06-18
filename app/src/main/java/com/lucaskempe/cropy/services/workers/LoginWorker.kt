package com.lucaskempe.cropy.services.workers

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.common.util.concurrent.ListenableFuture
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.providers.data.SplashUIDataStore
import com.lucaskempe.cropy.providers.data.SplashUIModel
import com.lucaskempe.cropy.providers.data.UserSessionDataStore
import com.lucaskempe.cropy.services.models.request.LoginRequest
import com.lucaskempe.cropy.services.models.response.LoginResponse
import com.lucaskempe.cropy.services.network.RetrofitClient
import com.lucaskempe.cropy.services.session.UserSession
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltWorker
class LoginWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val newContext = context
    private val userName = inputData.getString("username")
    private val password = inputData.getString("password")

    private val outputData = mutableStateOf(workDataOf("token" to ""))

    companion object {
        const val baseUrl: String = BuildConfig.BASE_URL
    }


    override suspend fun doWork(): Result {
        try {
            val client = RetrofitClient(baseUrl,newContext)
            val call = client.loginAPI.login(LoginRequest(userName!!, password!!))
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    p0: Call<LoginResponse>,
                    p1: Response<LoginResponse>
                ) {
                    if (p1.isSuccessful) {
                        val token = p1.body()?.token!!
                        CoroutineScope(Dispatchers.IO).launch {
                            newContext.UserSessionDataStore.updateData { UserSession(0, userName, password, token) }
                            outputData.value = workDataOf("token" to token)
                        }
                    }
                }

                override fun onFailure(p0: Call<LoginResponse>, p1: Throwable) {
                    Log.d("Login", "${p1.message}, ${p1.cause}")
                }

            })
            delay(3000)
            return Result.success(outputData.value)
        } catch (e: Exception) {
            return Result.failure()
        }
    }

}