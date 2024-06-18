package com.lucaskempe.cropy.services.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.providers.data.UserSessionDataStore
import com.lucaskempe.cropy.services.models.request.EmailRequest
import com.lucaskempe.cropy.services.models.request.LoginRequest
import com.lucaskempe.cropy.services.models.response.LoginResponse
import com.lucaskempe.cropy.services.network.RetrofitClient
import com.lucaskempe.cropy.services.session.UserSession
import com.lucaskempe.cropy.services.workers.LoginWorker.Companion
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltWorker
class MailRequestWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : Worker(context, workerParams)  {

    private val newContext = context
    private val email = inputData.getString("email")!!

    companion object {
        const val baseUrl: String = BuildConfig.BASE_URL
    }

    override fun doWork(): Result {
        val client = RetrofitClient(baseUrl,newContext)
        val call = client.mailRequestAPI.requestEmail(EmailRequest(email))
        call.enqueue(object : Callback<VerificationResponse> {
            override fun onResponse(
                p0: Call<VerificationResponse>,
                p1: Response<VerificationResponse>
            ) {
                if (p1.isSuccessful) {
                    val result = p1.body()?.string
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("MAIL_REQUEST", "$result")
                    }
                }
            }

            override fun onFailure(p0: Call<VerificationResponse>, p1: Throwable) {
                Log.d("MAIL_REQUEST", "${p1.message}, ${p1.cause}")
            }

        })
        return Result.success()
    }

    data class VerificationResponse(
        var string: String = ""
    )

}