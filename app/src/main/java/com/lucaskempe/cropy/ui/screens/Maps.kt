package com.lucaskempe.cropy.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.lucaskempe.cropy.viewmodels.MapsViewModel

@Composable
fun MapScreen(vm: MapsViewModel) {

    val context = LocalContext.current

    val mapDrawable: MutableState<Bitmap> = remember {
        mutableStateOf(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.Builder()
            .target(LatLng(vm.currentLocation.latitude, vm.currentLocation.longitude))
            .bearing(vm.currentLocation.bearing)
            .tilt(75f)
            .zoom(15f)
            .build()
    }

    val imageLoader = ImageLoader(context).newBuilder().build()
    val request = ImageRequest.Builder(context)
        .data("https://cdn.icon-icons.com/icons2/678/PNG/512/Google_icon-icons.com_60916.png")
        .crossfade(true)
        .build()

    LaunchedEffect(Unit) {
        vm.getRoute(LatLng(vm.currentLocation.longitude, vm.currentLocation.longitude), vm.midP, LatLng(vm.startPoint.latitude, vm.startPoint.longitude) )
        val result = imageLoader.execute(request).drawable
        mapDrawable.value = Bitmap.createScaledBitmap((result as BitmapDrawable).bitmap, 130, 130, false)
    }

    LaunchedEffect(key1= cameraPositionState.isMoving) { vm.onMapMoved(cameraPositionState.position.target) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true),
    ) {
        Marker(
            icon = BitmapDescriptorFactory.fromBitmap(mapDrawable.value),
            state = rememberMarkerState(position = LatLng(vm.currentLocation.latitude, vm.currentLocation.longitude)
            ),
        )
    }

}