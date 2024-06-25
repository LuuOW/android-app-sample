package com.lucaskempe.cropy.services.maps

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.providers.data.DistanceDataStore
import com.lucaskempe.cropy.services.maps.models.distance.DistanceModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltWorker
class DistanceWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams)  {

    val newContext = context

    val origin = inputData.getString("origin")!!
    val destination = inputData.getString("destination")!!

    override suspend fun doWork(): Result {
        val token = runBlocking { BuildConfig.DIRECTIONS_KEY } // Run the suspend function in a blocking way
        val client = RetrofitCall(newContext).distanceAPI
        val call = client.getDistance(destination,origin,token,"metric")
        call.enqueue(object: Callback<DistanceModel> {

            override fun onResponse(p0: Call<DistanceModel>, p1: Response<DistanceModel>) {
                if (p1.isSuccessful) {
                    CoroutineScope(Dispatchers.IO+ Job()).launch {
                        newContext.DistanceDataStore.updateData { p1.body()!! }
                    }
                }
            }

            override fun onFailure(p0: Call<DistanceModel>, p1: Throwable) {
                Log.d("DISTANCE_WORKER", "ERROR")
            }
        })

        return Result.success()
    }

}