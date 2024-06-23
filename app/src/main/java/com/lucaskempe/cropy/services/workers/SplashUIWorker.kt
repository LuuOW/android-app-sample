package com.lucaskempe.cropy.services.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.providers.data.SplashUIDataStore
import com.lucaskempe.cropy.providers.data.SplashUIModel
import com.lucaskempe.cropy.services.network.RetrofitClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltWorker
class SplashUIWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val newContext = context
    private val componentId = inputData.getString("id")

    companion object {
        const val baseUrl: String = BuildConfig.BASE_URL
    }

    override fun doWork(): Result {
        val client = RetrofitClient(baseUrl, newContext)
        val call = client.splashAPI.getSplashUI(componentId!!)
        call.enqueue(object : Callback<SplashUIModel> {
            override fun onResponse(
                p0: Call<SplashUIModel>,
                p1: Response<SplashUIModel>
            ) {
                if (p1.isSuccessful) {
                    val splash = p1.body()
                    CoroutineScope(Dispatchers.IO).launch {
                        newContext.SplashUIDataStore.updateData { splash!! }
                    }
                }            }

            override fun onFailure(p0: Call<SplashUIModel>, p1: Throwable) {
                Log.d("splash", "${p1.message}, ${p1.cause}")
            }

        })
        return Result.success()
    }


}