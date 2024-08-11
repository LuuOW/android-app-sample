package com.cropy.maps.models

enum class RoutingPreference {
    ROUTING_PREFERENCE_UNSPECIFIED,
    TRAFFIC_UNAWARE,
    TRAFFIC_AWARE,
    TRAFFIC_AWARE_OPTIMAL
}

enum class PolylineQuality {
    POLYLINE_QUALITY_UNSPECIFIED,
    HIGH_QUALITY,
    OVERVIEW
}

enum class PolylineEncoding {
    POLYLINE_ENCODING_UNSPECIFIED,
    ENCODED_POLYLINE,
    GEO_JSON_LINESTRING
}

enum class Units {
    UNITS_UNSPECIFIED,
    METRIC,
    IMPERIAL
}

enum class TrafficModel {
    TRAFFIC_MODEL_UNSPECIFIED,
    BEST_GUESS,
    PESSIMISTIC,
    OPTIMISTIC
}

data class RouteRequest(
    val origin: Waypoint,
    val destination: Waypoint,
    val intermediates: List<Waypoint>? = listOf(),
    val travelMode: RouteTravelMode = RouteTravelMode.DRIVE,
    val routingPreference: RoutingPreference = RoutingPreference.TRAFFIC_UNAWARE,
    val polylineQuality: PolylineQuality = PolylineQuality.OVERVIEW,
    val polylineEncoding: PolylineEncoding = PolylineEncoding.ENCODED_POLYLINE,
    val departureTime: String? = null,
    val extraComputations: List<String>? = null,
    val computeAlternativeRoutes: Boolean = false,
    val routeModifiers: RouteModifiers = RouteModifiers(
        avoidTolls = false,
        avoidHighways = false,
        avoidFerries = false
    ),
    val languageCode: String = "en-GB",
    val units: Units = Units.METRIC,
    val optimizeWaypointOrder: Boolean = false,
    val trafficModel: TrafficModel? = TrafficModel.TRAFFIC_MODEL_UNSPECIFIED
)