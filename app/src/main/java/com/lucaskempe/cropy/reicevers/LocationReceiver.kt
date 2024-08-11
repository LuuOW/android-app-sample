package com.lucaskempe.cropy.reicevers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import com.lucaskempe.cropy.viewmodels.MapsViewModel

class LocationReceiver(
    private val viewModel: MapsViewModel
): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val location = intent.getParcelableExtra<Location>("LOCATION_UPDATE")
        if (location != null) {
            viewModel.currentLocation = location
        }
    }

}