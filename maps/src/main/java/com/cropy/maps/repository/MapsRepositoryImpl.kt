package com.cropy.maps.repository

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MapsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val routesAPI: com.cropy.maps.network.RoutesAPI
): MapsRepository {

    override suspend fun fetchRoute(origin: LatLng, destination: LatLng, intermediaryWaypoints: List<LatLng>): Result<com.cropy.maps.models.Route> {
        val routesRequest = com.cropy.maps.models.RouteRequest(
            origin = com.cropy.maps.models.Waypoint(location = com.cropy.maps.models.Location(origin)),
            destination = com.cropy.maps.models.Waypoint(
                location = com.cropy.maps.models.Location(
                    destination
                )
            ),
            intermediates = intermediaryWaypoints.map {
                com.cropy.maps.models.Waypoint(
                    location = com.cropy.maps.models.Location(
                        it
                    )
                )
            },
            routingPreference = com.cropy.maps.models.RoutingPreference.TRAFFIC_AWARE,
            extraComputations = listOf("TRAFFIC_ON_POLYLINE"),
            polylineQuality = com.cropy.maps.models.PolylineQuality.HIGH_QUALITY
        )

        val response = routesAPI.fetchDirections(routesRequest)
        val routeData = response.body()
        if (response.isSuccessful && routeData != null) {
            return Result.success(routeData)
        } else {
            return Result.failure(Throwable("error in maps repository requesting route"))
        }

    }

}