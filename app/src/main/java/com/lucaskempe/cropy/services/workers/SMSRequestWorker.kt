package com.lucaskempe.cropy.services.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.request.SMSRequest
import com.lucaskempe.cropy.services.models.response.SMSResponse
import com.lucaskempe.cropy.services.network.RetrofitClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SMSRequestWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val baseUrl: String = BuildConfig.BASE_URL
    }

    private val newContext = context

    private val phone = inputData.getString("phone")!!

    override suspend fun doWork(): Result {
        val client = RetrofitClient(baseUrl, newContext).smsRequestAPI
        val call = client.requestSMS(SMSRequest(phone))
        call.enqueue(object: Callback<SMSResponse> {

            override fun onResponse(p0: Call<SMSResponse>, p1: Response<SMSResponse>) {

                if (p1.isSuccessful) {
                    Log.d("SMS_REQUEST", "SUCCESS")
                }

            }

            override fun onFailure(p0: Call<SMSResponse>, p1: Throwable) {
                Log.d("SMS_REQUEST", "FAILURE")
            }

        })


        return Result.success()
    }

}