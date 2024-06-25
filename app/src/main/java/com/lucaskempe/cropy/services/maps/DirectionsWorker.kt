package com.lucaskempe.cropy.services.maps

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.providers.data.Directions2DataStore
import com.lucaskempe.cropy.providers.data.DirectionsDataStore
import com.lucaskempe.cropy.services.maps.models.Directions
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltWorker
class DirectionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    val newContext = context

    val origin = inputData.getString("origin")!!
    val destination = inputData.getString("destination")!!

    override suspend fun doWork(): Result {
        try {
            val token = runBlocking { BuildConfig.DIRECTIONS_KEY } // Run the suspend function in a blocking way

            val client = RetrofitCall(newContext).directionsAPI
            val call = client.getDirectionsTo(origin, destination, token)
            call.enqueue(object: Callback<Directions> {

                override fun onResponse(p0: Call<Directions>, p1: Response<Directions>) {
                    if (p1.isSuccessful) {
                        println(p1.body())
                        CoroutineScope(Dispatchers.IO).launch {
                            newContext.DirectionsDataStore.updateData {
                                p1.body()!!
                            }
                        }

                    }
                }

                override fun onFailure(p0: Call<Directions>, p1: Throwable) {
                    println("Error: ${p1.cause}")
                }

            })

            return Result.success()
        }catch (e: Exception) {
            return Result.failure()
        }
    }

}