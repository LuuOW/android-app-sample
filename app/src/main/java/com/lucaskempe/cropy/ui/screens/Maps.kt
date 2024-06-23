package com.lucaskempe.cropy.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import android.util.Log
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.DesignElements.map
import androidx.navigation.NavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.StyleSpan
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucaskempe.cropy.providers.data.Directions2DataStore
import com.lucaskempe.cropy.providers.data.DirectionsDataStore
import com.lucaskempe.cropy.services.maps.DirectionsWorker
import com.lucaskempe.cropy.services.maps.LatLngDirection
import com.lucaskempe.cropy.services.maps.models.Directions
import com.lucaskempe.cropy.services.maps.models.Polyline
import com.lucaskempe.cropy.services.models.users
import com.lucaskempe.cropy.services.workers.LoginWorker
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    modifier: Modifier,
    controller: NavController,
    context: Context,
    vm: UsersViewModel,
    onClick: () -> Unit = { }
) {

    val mapDrawable: MutableState<Bitmap> = remember {
        mutableStateOf(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
    }

    val workManager = WorkManager.getInstance(context)

    var directions by remember { mutableStateOf(Directions()) }

    var currentToStart by remember {
        mutableStateOf(Directions())
    }

    val userData = users.find { it.id == vm.currUserId } ?: throw Exception("User not found")


    if (userData.address.isNotEmpty()) {

        val geocoder = Geocoder(context, Locale.getDefault())
        val location = geocoder.getFromLocationName(userData.address, 10)
        val locationAddress = location?.first()

        val locationProvider = LocationServices.getFusedLocationProviderClient(context)
        var current by remember {
            mutableStateOf(LatLng(0.0,0.0))
        }

        val city = locationAddress?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0)


        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom( city , 12.5f)
        }

        val imageLoader = ImageLoader(context).newBuilder().build()
        val request = ImageRequest.Builder(context)
            .data(userData.image)
            .crossfade(true)
            .build()

        LaunchedEffect(
            key1 = vm,
            key2 = context
        ) {
            val result = imageLoader.execute(request).drawable
            mapDrawable.value =
                Bitmap.createScaledBitmap((result as BitmapDrawable).bitmap, 130, 130, false)

            locationProvider.getCurrentLocation(Priority.PRIORITY_LOW_POWER, CancellationTokenSource().token).addOnSuccessListener { location ->
                current = LatLng(location.latitude,location.longitude)
            }

        }


        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true),
        ) {
            Marker(
                icon = BitmapDescriptorFactory.fromBitmap(mapDrawable.value),
                state = MarkerState(position = city),
                title = "Ciudad",
                onInfoWindowClick = {
                   controller.navigate("home") {
                        popUpTo("home") { inclusive=true }
                    }
                },
                snippet = "Marcado en ciudad"
            )

            LaunchedEffect(key1 = workManager, key2 = cameraPositionState) {

                if (true) {
                    delay(2000)

                    val workRequest = OneTimeWorkRequest.Builder(DirectionsWorker::class.java)
                        .setInputData(workDataOf(
                            "current" to "Correo Argentino Sucursal 5 La Plata,AR", "starting" to "Plaza España La Plata,AR",
                            "origin" to "Plaza Matheu La Plata, AR", "destination" to "Plaza España La Plata,AR"
                        )).build()
                    workManager.enqueueUniqueWork("one_time", ExistingWorkPolicy.KEEP, workRequest)

                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom( city , 15f)),
                        durationMs = 2000
                    )
                    delay(300)

                    directions = context.DirectionsDataStore.data.first()

                    currentToStart = context.Directions2DataStore.data.first()

                }

            }

            if (directions.status != null) {

                directions.routes.first().legs.first().steps.forEach {
                    Polyline(
                        color = Color.Green,
                        points = listOf(LatLng(it.endLocation?.lat!!, it.endLocation?.lng!!), LatLng(it.startLocation?.lat!!, it.startLocation?.lng!!)))
                }

                currentToStart.routes.first().legs.first().steps.forEach {
                    Polyline(
                        color = Color.Green,
                        points = listOf(LatLng(it.endLocation?.lat!!, it.endLocation?.lng!!), LatLng(it.startLocation?.lat!!, it.startLocation?.lng!!)))
                }
            }


        }
    } else {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            properties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true)
        )
    }
}