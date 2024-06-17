package com.lucaskempe.cropy.services.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.request.SignUpRequest
import com.lucaskempe.cropy.services.models.response.SignUpResponse
import com.lucaskempe.cropy.services.network.RetrofitClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val baseUrl: String = BuildConfig.BASE_URL
    }

    private val username = inputData.getString("username")!!
    private val email = inputData.getString("email")!!
    private val password = inputData.getString("password")!!
    private val role = inputData.getString("role")!!

    override suspend fun doWork(): Result {
        try {
            val client = RetrofitClient(baseUrl).signupAPI
            val call = client.signUp(SignUpRequest(username,email, password, role))
            call.enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(p0: Call<SignUpResponse>, p1: Response<SignUpResponse>) {
                    Log.d("SIGN_UP", "Success")
                }

                override fun onFailure(p0: Call<SignUpResponse>, p1: Throwable) {
                    Log.d("SIGN_UP", "FAIL")
                }
            })
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

}