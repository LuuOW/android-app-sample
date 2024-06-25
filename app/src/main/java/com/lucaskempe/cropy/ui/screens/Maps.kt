package com.lucaskempe.cropy.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucaskempe.cropy.services.maps.MapRepository
import com.lucaskempe.cropy.services.maps.MapsViewModel
import com.lucaskempe.cropy.services.models.users
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

@SuppressLint("MissingPermission", "CoroutineCreationDuringComposition")
@Composable
fun MapScreen(
    modifier: Modifier,
    controller: NavController,
    context: Context,
    vm: UsersViewModel,
    onClick: () -> Unit = { }
) {

    val mapVM: MapsViewModel = hiltViewModel()

    val repository = MapRepository(context, mapVM)

    val mapDrawable: MutableState<Bitmap> = remember {
        mutableStateOf(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
    }
    val userData = users.find { it.id == vm.currUserId } ?: throw Exception("User not found")

    if (userData.address.isNotEmpty()) {

        val geocoder = Geocoder(context, Locale.getDefault())
        val location = geocoder.getFromLocationName(userData.address, 10)
        val locationAddress = location?.first()

        val locationProvider = LocationServices.getFusedLocationProviderClient(context)

        val city = locationAddress?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(city, 12.5f)
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

            while (true) {
                delay(3000)
                locationProvider.getCurrentLocation(Priority.PRIORITY_LOW_POWER, CancellationTokenSource().token).addOnSuccessListener { location ->
                    repository.fetchRoute("${location.latitude},${location.longitude}", "Correo Argentino Surcursal 5, La Plata, Argentina")
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            val zoomPosition = CameraPosition
                .builder()
                .target(city)
                .zoom(15f)
                .build()
            val cameraFactory = CameraUpdateFactory.newCameraPosition(zoomPosition)
            cameraPositionState.animate(update = cameraFactory, durationMs = 2000)
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
                        popUpTo("home") { inclusive = true }
                    }
                },
                snippet = "Marcado en ciudad"
            )

            LaunchedEffect(key1 = cameraPositionState) {
                while (true) {
                    delay(5000)
                    repository.updateRoute()
                }
            }

            mapVM.steps.forEach {
                Polyline(
                    color = Color.Green,
                    points = listOf(
                        LatLng(it.endLocation.lat, it.endLocation.lng),
                        LatLng(it.startLocation.lat, it.startLocation.lng)
                    )
                )
            }

        }
    } else {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            properties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true)
        )
    }
}