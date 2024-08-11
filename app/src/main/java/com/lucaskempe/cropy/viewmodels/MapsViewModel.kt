package com.lucaskempe.cropy.viewmodels

import android.content.Context
import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.cropy.maps.isLocationClose
import com.cropy.maps.models.GooglePlace
import com.cropy.maps.models.Route
import com.cropy.maps.repository.MapsRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
    private val mapsRepository: MapsRepository
) : ViewModel() {

    var currentLocation by stateHandle.saveable { mutableStateOf(Location("")) }

    val route by stateHandle.saveable { mutableStateOf(Route()) }

    var isCenteredOnLocation by stateHandle.saveable { mutableStateOf(false) }
    var recenter by stateHandle.saveable { mutableStateOf(false) }

    val placeList = listOf(
        GooglePlace(
            "22 Wooler st",
            "",
            51.487491,
            -0.089637,
            listOf("Sir William Shakespeare", "Sir Playstation"),
            LocalDate.now().atTime(
                LocalTime.now().plus(2, ChronoUnit.MINUTES)
            ).toString()
        ),
        GooglePlace(
            "6 Brettell st",
            "",
            51.487223,
            -0.088282,
            listOf("Sir Thomas Wyatt", "Sir Maccan", "Lady Xbox"),
            LocalDate.now().atTime(
                LocalTime.now().minus(2, ChronoUnit.MINUTES)
            ).toString()
        ),
        GooglePlace(
            "10 Villa St",
            "",
            51.486940,
            -0.088753,
            listOf("William Blake", "Lady Tarcic", "Sir Robert"),
            LocalDate.now().atTime(
                LocalTime.now().plus(2, ChronoUnit.MINUTES)
            ).toString()
        ),
    )

    val startPoint = GooglePlace(
        "76 Liverpool grove st",
        "",
        51.486657,
        -0.091072,
        listOf("Sir Philip Sidney", "Lady Nintendo", "Robin Loud"),
        LocalDate.now().atTime(LocalTime.now().plus(30, ChronoUnit.SECONDS)).toString()
    )

    val midP = mutableListOf<LatLng>()

    init {
        midP.add(LatLng(startPoint.latitude, startPoint.longitude))
        midP.addAll(placeList.dropLast(1) .map { waypoint -> LatLng(waypoint.latitude, waypoint.longitude) })
    }

    fun onMapMoved(mapTargetLocation: LatLng) {
        isCenteredOnLocation = mapTargetLocation.isLocationClose(LatLng(currentLocation.latitude, currentLocation.longitude), 60f)
    }

    fun onCenteredLocation() {
        isCenteredOnLocation = true
        recenter = !recenter
    }

    fun onRecenterTap() {
        recenter = !recenter
    }

    fun getRoute(source: LatLng, midpoints: List<LatLng>?, destiny: LatLng) {
        val waypoints = mutableListOf<LatLng>()
        if (midpoints != null) {
            waypoints.addAll(midpoints)
        }

        viewModelScope.launch {
            mapsRepository.fetchRoute(source, destiny, waypoints)
        }
    }

}

fun latLngListToDoubleArray(latLngList: List<LatLng>): DoubleArray {
    val doubleArray = DoubleArray(latLngList.size * 2)
    var index = 0
    for (latLng in latLngList) {
        doubleArray[index++] = latLng.latitude
        doubleArray[index++] = latLng.longitude
    }
    return doubleArray
}