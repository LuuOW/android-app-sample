package com.cropy.maps.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class RouteLabel {
    ROUTE_LABEL_UNSPECIFIED,
    DEFAULT_ROUTE,
    DEFAULT_ROUTE_ALTERNATE,
    FUEL_EFFICIENT
}

@Parcelize
data class Route(
    val routeLabels: List<RouteLabel>? = null,
    val legs: List<RouteLeg>? = null,
    val distanceMeters: Int? = null,
    val duration: String? = null,
    val staticDuration: String? = null,
    val polyline: Polyline? = null,
    val description: String? = null,
    val warnings: List<String>? = null,
    val viewport: Viewport? = null,
    val travelAdvisory: RouteTravelAdvisory? = null,
    val optimizedIntermediateWaypointIndex: List<Int>? = null,
    val localizedValues: List<RouteLocalizedValues>? = null,
    val routeToken: String? = null
): Parcelable