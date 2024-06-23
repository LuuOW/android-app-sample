package com.lucaskempe.cropy.services.maps

import com.google.gson.annotations.SerializedName

data class DirectionsModel(
    @SerializedName("geocoded_waypoints")
    val geocodedWaypoints: Array<WayPoint>,
    val routes: Array<DirectionRoute>,
    val status: String
)

data class DirectionRoute(
    val bounds: BoundDirection,
    val copyrights: String,
    val legs: Array<String>,
    val overview_polyline: PolylinePoints,
    val summary: String,
    val warnings: Array<String>,
    val waypoint_order: Array<String>
)

data class PolylinePoints(
    val points: String
)

data class LegDirection(
    val distance: LegDistanceDuration,
    val duration: LegDistanceDuration,
    val end_address: String,
    val end_location: LatLngDirection,
    val start_address: String,
    val start_location: LatLngDirection,
    val steps: String,
    val traffic_speed_entry: Array<String>,
    val via_waypoint: Array<String>
)

data class LegDistanceDuration(
    val text: String,
    val value: Int
)

data class WayPoint(
    @SerializedName("geocode_status")
    val geocodeStatus: String,
    @SerializedName("place_id")
    val placeId: String,
    val types: Array<String>
)

data class BoundDirection(
    val northeast: LatLngDirection,
    val southwest: LatLngDirection
)

data class LatLngDirection(
    val lat: Float,
    val lng: Float
)

