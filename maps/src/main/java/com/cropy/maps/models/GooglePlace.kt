package com.cropy.maps.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GooglePlace(
    val address: String,
    val googlePlaceId: String?,
    val latitude: Double,
    val longitude: Double,
    val riders: List<String>?,
    val pickupTime: String?
): Parcelable