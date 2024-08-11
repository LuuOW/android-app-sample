package com.cropy.maps.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

enum class RouteTravelMode {
    TRAVEL_MODE_UNSPECIFIED,
    DRIVE,
    BICYCLE,
    WALK,
    TWO_WHEELER,
    TRANSIT
}

enum class Maneuver {
    MANEUVER_UNSPECIFIED,
    TURN_SLIGHT_LEFT,
    TURN_SHARP_LEFT,
    UTURN_LEFT,
    TURN_LEFT,
    TURN_SLIGHT_RIGHT,
    TURN_SHARP_RIGHT,
    UTURN_RIGHT,
    TURN_RIGHT,
    STRAIGHT,
    RAMP_LEFT,
    RAMP_RIGHT,
    MERGE,
    FORK_LEFT,
    FORK_RIGHT,
    FERRY,
    FERRY_TRAIN,
    ROUNDABOUT_LEFT,
    ROUNDABOUT_RIGHT,
    DEPART,
    NAME_CHANGE
}

@Parcelize
data class RouteLeg(
    val distanceMeters: Int? = null,
    val duration: String? = null,
    val staticDuration: String? = null,
    val polyline: Polyline? = null,
    val startLocation: Location? = null,
    val endLocation: Location? = null,
    val steps: List<RouteLegStep>? = null,
    val travelAdvisory: RouteTravelAdvisory? = null,
    val localizedValues: RouteLocalizedValues? = null,
    val stepsOverview: StepsOverview? = null
): Parcelable {

    @Parcelize
    data class RouteLegStep(
        val distanceMeters: Int? = 0,
        val staticDuration: String? = "0",
        val polyline: Polyline? = Polyline(""),
        val startLocation: Location? = Location(LatLng(0.0,0.0)),
        val endLocation: Location? = Location(LatLng(0.0,0.0)),
        val navigationInstruction: NavigationInstruction = NavigationInstruction(Maneuver.DEPART, ""),
        val travelAdvisory: RouteTravelAdvisory? = RouteTravelAdvisory(),
        val localizedValues: RouteLocalizedValues? = RouteLocalizedValues(),
        val travelMode: RouteTravelMode = RouteTravelMode.DRIVE
    ): Parcelable

    data class LatLong(
        val latitude: Float,
        val longitude: Float
    )

    @Parcelize
    data class StepsOverview(
        val multiModalSegments: List<MultiModalSegment>? = null
    ): Parcelable
}

@Parcelize
data class LocalizedText(
    val text: String,
    val languageCode: String
): Parcelable

@Parcelize
data class SpeedReadingInterval(
    val startPolylinePointIndex: Int? = null,
    val endPolylinePointIndex: Int? = null,
    val speed: Speed? = null
): Parcelable {
    enum class Speed {
        SPEED_UNSPECIFIED,
        NORMAL,
        SLOW,
        TRAFFIC_JAM
    }
}

@Parcelize
data class Money(
    val currencyCode: String,
    val units: String,
    val nanos: Int
): Parcelable

@Parcelize
data class TollInfo(
    val estimatedPrice: List<Money>? = null
): Parcelable

@Parcelize
data class NavigationInstruction(
    val maneuver: Maneuver,
    val instructions: String
): Parcelable

@Parcelize
data class MultiModalSegment(
    val navigationInstruction: NavigationInstruction,
    val travelMode: RouteTravelMode,
    val stepStartIndex: Int,
    val stepEndIndex: Int
): Parcelable

@Parcelize
data class Polyline(
    val encodedPolyline: String
): Parcelable

@Parcelize
data class Viewport(
    val low: LatLng,
    val high: LatLng
): Parcelable

@Parcelize
data class RouteTravelAdvisory(
    val tollInfo: TollInfo? = null,
    val speedReadingIntervals: List<SpeedReadingInterval>? = null,
    val fuelConsumptionMicroliters: String? = null,
    val routeRestrictionsPartiallyIgnored: Boolean? = null,
    val transitFare: Money? = null
): Parcelable

@Parcelize
data class RouteLocalizedValues(
    val distance: LocalizedText? = null,
    val duration: LocalizedText? = null,
    val staticDuration: LocalizedText? = null,
    val transitFare: LocalizedText? = null
): Parcelable