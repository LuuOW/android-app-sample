package com.cropy.maps.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Waypoint(
    val via: Boolean = false,
    val vehicleStopover: Boolean = false,
    val sideOfRoad: Boolean = false,
    val location: Location,
    val heading: Int? = null
): Parcelable

@Parcelize
data class Location(
    val latLng: LatLng
): Parcelable