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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucaskempe.cropy.services.models.users
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
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

    val userData = users.find { it.id == vm.currUserId } ?: throw Exception("User not found")


    if (userData.address.isNotEmpty()) {

        val geocoder = Geocoder(context, Locale.getDefault())
        val location = geocoder.getFromLocationName(userData.address, 10)
        val locationAddress = location?.first()

        val city = locationAddress?.let { LatLng(it.latitude, it.longitude) } ?: LatLng(0.0, 0.0)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(city, 15f)
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
        }


        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true)
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

        }
    } else {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            properties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true)
        )
    }
}