package com.cropy.maps

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun LatLng.isLocationClose(targetLocation: LatLng, closeDistanceMeters: Float = 10f): Boolean {
    val results = FloatArray(1)
    Location.distanceBetween(
        this.latitude, this.longitude,
        targetLocation.latitude, targetLocation.longitude,
        results
    )
    return results[0] <= closeDistanceMeters
}

fun LatLng.distanceTo(targetLocation: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(
        this.latitude, this.longitude,
        targetLocation.latitude, targetLocation.longitude,
        results
    )
    return results[0]
}