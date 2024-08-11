package com.cropy.maps.repository

import com.google.android.gms.maps.model.LatLng

interface MapsRepository {

    suspend fun fetchRoute(origin: LatLng, destination: LatLng, intermediaryWaypoints: List<LatLng> = listOf()): Result<com.cropy.maps.models.Route>

}