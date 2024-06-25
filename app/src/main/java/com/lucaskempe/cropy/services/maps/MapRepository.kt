package com.lucaskempe.cropy.services.maps

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.lucaskempe.cropy.providers.data.DirectionsDataStore
import com.lucaskempe.cropy.providers.data.DistanceDataStore
import com.lucaskempe.cropy.services.maps.models.distance.Distance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MapRepository(
    val context: Context,
    val mapVM: MapsViewModel
){


    val workManager = WorkManager.getInstance(context)

    fun fetchRoute(origin: String, destination: String) {
        val workRequest = OneTimeWorkRequest.Builder(DirectionsWorker::class.java)
            .setInputData(
                workDataOf(
                    "origin" to origin,
                    "destination" to destination
                )
            ).build()
        workManager.enqueueUniqueWork("one_time", ExistingWorkPolicy.KEEP, workRequest)
    }


    fun updateRoute(){
        CoroutineScope(Dispatchers.IO).launch {
            context.DirectionsDataStore.data.collect {
                mapVM.updateSteps(it.routes.first().legs.first().steps)
            }
        }
    }

    fun getDistance(): Distance {
        val distance = runBlocking { context.DistanceDataStore.data.first().rows.first().elements.first().distance }
        return distance ?: Distance(text = "", value = -1)
    }


}