package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
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
        animationSpec = tween(durationMillis = 1000, delayMillis = 300),
        targetValue = data.offSet.value,
        label = "offset",
        finishedListener = { }
    )

    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 15.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = AbsoluteRoundedCornerShape(24.dp),
        modifier = modifier
            .offset { offset }
            .background(Color.Transparent )
            .onGloballyPositioned {
                onPosition(it.positionInRoot().round(), data.id)
            }
            .clickable() {
                onClick()
            }
            .padding(8.dp)
            .size(80.dp)
    ) {
        AsyncImage(
            model = imageURLForItems,
            contentDescription = "Icon",
            modifier = modifier
                .size(80.dp)
                .clip(CircleShape)
        )
    }

}