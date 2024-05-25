package com.lucaskempe.cropy.ui.screens.items

import android.net.ConnectivityManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.core.content.ContextCompat.getSystemService
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomButton(
    onClick: () -> Unit = {},
    onAnimationFinish: () -> Unit = { },
    onPosition: (IntOffset, Int) -> Unit = { offSet, iD -> },
    data: ButtonData,
) {

    val modifier = Modifier

    val imageURLForItems = data.icon

    val offset by animateIntOffsetAsState(
        animationSpec = tween(durationMillis = 700, delayMillis = 100),
        targetValue = data.offSet.value,
        label = "offset",
        finishedListener = { onAnimationFinish() }
    )

    val connectivityManager =
        getSystemService(LocalContext.current, ConnectivityManager::class.java)
    val activeNetwork = connectivityManager?.activeNetwork

    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 15.dp) ,
        colors = CardDefaults.elevatedCardColors(containerColor = if (data.icon.isNotEmpty()) Color.White else Color.Transparent),
        shape = AbsoluteRoundedCornerShape(24.dp),
        modifier = modifier
            .offset { offset }
            .background(Color.Transparent)
            .onGloballyPositioned { onPosition(it.positionInRoot().round(), data.id) }
            .clickable() {
                onClick()
            }
            .padding(8.dp)
            .size(80.dp)
    ) {
        // if no internet circle shape will be shown
        if (activeNetwork != null) {
            AsyncImage(
                model = imageURLForItems,
                contentDescription = "Icon",
                modifier = modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        } else {
            Box {
                Canvas(
                    modifier = modifier
                        .padding(start = 30.dp, end = 30.dp, bottom = 18.dp)
                        .size(35.dp)
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .align(Alignment.Center)

                ) {
                    drawCircle(
                        color = Color(70, 156, 221),
                        radius = 35.dp.toPx()
                    )
                }

            }
        }

    }

}

@Preview
@Composable
fun CustomButtonPreview() {
    val data = ButtonData(
        id = 1,
        icon = "https://clipart-library.com/image_gallery/149336.png",
        title = "Buy burguer",
        urlAction = "https://google.com",
        offSet = remember {
            mutableStateOf(IntOffset(0,80))
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton( data=data)
    }
}

@Composable
fun SimpleColorChangeAnimationPreview() {
    var color by remember { mutableStateOf(Color.Red) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = animateColorAsState(
                        targetValue = color,
                        animationSpec = tween(durationMillis = 1000)
                    ).value
                )
                .clickable {
                    color = if (color == Color.Red) Color.Blue else Color.Red
                }
        )
    }
}