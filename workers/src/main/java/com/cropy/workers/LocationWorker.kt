package com.cropy.workers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Looper
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {


    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        val request = LocationRequest.Builder(1000).build()
        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val intent = Intent("ACTION_LOCATION_UPDATE")
                    intent.putExtra("LOCATION_UPDATE", it)
                    context.sendBroadcast(intent)
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

        return Result.success(workDataOf("LOCATION" to "LOCATION_WORKER"))
    }

}