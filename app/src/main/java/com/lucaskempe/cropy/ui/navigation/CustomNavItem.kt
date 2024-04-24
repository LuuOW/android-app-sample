package com.lucaskempe.cropy.ui.navigation

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


@Composable fun CustomNavItem(
    context: Context,
    onClick: () -> Unit,
    selected: () -> Boolean,
    navController: NavController
) {
    // jpg icon image
    val imageURL = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png"
    val imageLoader = ImageLoader(context).newBuilder().build()
    val request = ImageRequest.Builder(context)
        .data(imageURL)
        .size(150,150)
        .crossfade(true)
        .build()

    val asyncPainter = rememberAsyncImagePainter(model = request, imageLoader = imageLoader)

    val defaultBitmap: MutableState<Bitmap> = remember {
        mutableStateOf(Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888))
    }

/*    LaunchedEffect(
        key1 = imageLoader,
        key2 = request
    ) {
        val drawable = imageLoader.execute(request).drawable
        defaultBitmap.value = Bitmap.createScaledBitmap((drawable as BitmapDrawable).bitmap, 130, 130, false)

    }*/

    IconButton(
        modifier = Modifier.background(
            color = Color.White
        ),
        onClick = { /*TODO*/ }) {


        Icon(
            painter = asyncPainter,
            contentDescription = null
        )
    }

}