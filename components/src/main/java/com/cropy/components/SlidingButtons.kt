package com.cropy.components

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.cropy.components.colors.AppColor
import kotlin.math.roundToInt
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.cropy.components.fonts.AppTypography


enum class SlidingButtonType {
    Primary,
    Secondary,
    Success,
    Critical
}

enum class CompleteThreshold {
    Easy, // in this setting the sliding button autocompletes after sliding 40% of the way
    Hard // in this setting the sliding button autocompletes after sliding 80% of the way
}

private object SlidingButtonDimen {
    val BUTTON_HEIGHT = 56.dp
    val BUTTON_WIDTH = 350.dp
    val INNER_SQUARE_SIZE = 40.dp
    val INNER_PADDING = 8.dp
}

@ExperimentalFoundationApi
@Composable
fun SlidingButton(
    text: String,
    modifier: Modifier = Modifier,
    buttonType: SlidingButtonType = SlidingButtonType.Primary,
    enabled: Boolean = true,
    progressLoading: Boolean = false,
    completeThreshold: CompleteThreshold = CompleteThreshold.Easy,
    onSlideComplete: () -> Unit,
) {
    if (!enabled) {
        SlidingButtonDisabled(
            modifier = modifier,
            text = text,
            buttonType = buttonType
        )
    } else if (progressLoading)
        SlidingButtonLoading(
            modifier = modifier,
            buttonType = buttonType
        )
    else
        SlidingButtonContent(
            modifier = modifier,
            text = text,
            buttonType = buttonType,
            completeThreshold = completeThreshold,
            onSlideComplete = onSlideComplete
        )
}

enum class DragValue { Start, Center, End }


@ExperimentalFoundationApi
@Composable
private fun SlidingButtonContent(
    text: String,
    modifier: Modifier = Modifier,
    buttonType: SlidingButtonType = SlidingButtonType.Primary,
    completeThreshold: CompleteThreshold = CompleteThreshold.Easy,
    onSlideComplete: () -> Unit,
) {
    val buttonWidth = SlidingButtonDimen.BUTTON_WIDTH
    val buttonHeight = SlidingButtonDimen.BUTTON_HEIGHT
    val squareSize = SlidingButtonDimen.INNER_SQUARE_SIZE
    val innerBoxPadding = SlidingButtonDimen.INNER_PADDING
    val limit = buttonWidth - squareSize - innerBoxPadding * 2 // how far can we drag the inner box

    val containerColor = when(buttonType) {
        SlidingButtonType.Primary -> AppColor.Primary
        SlidingButtonType.Secondary -> AppColor.Navy200
        SlidingButtonType.Success -> AppColor.Green
        SlidingButtonType.Critical -> AppColor.Red
    }

    val textColor = when(buttonType) {
        SlidingButtonType.Primary,
        SlidingButtonType.Critical,
        SlidingButtonType.Success -> AppColor.White
        SlidingButtonType.Secondary -> AppColor.Primary
    }

    val arrowStartColor = when(buttonType) {
        SlidingButtonType.Primary,
        SlidingButtonType.Secondary -> AppColor.Primary
        SlidingButtonType.Success -> AppColor.Green
        SlidingButtonType.Critical -> AppColor.Red
    }


    val density = LocalDensity.current
    val anchors = with (density) {
        DraggableAnchors {
            DragValue.Start at  0.dp.toPx()
            DragValue.Center at 348/2f
            DragValue.End at 348.dp.toPx()
        }
    }

    val state = remember { AnchoredDraggableState(
        initialValue = DragValue.Start,
        anchors = anchors,
        positionalThreshold = { it * 0.5f },
        animationSpec = tween(300),
        velocityThreshold = { 1000f }
    ) }

    SideEffect { state.updateAnchors(anchors) }

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == DragValue.End) {
            onSlideComplete()
        }
    }

    val limitPx = limit.dpToPx()

    val transparency = (1 - state.offset / limitPx).coerceIn(0f, 1f)

    val swipeOffset = IntOffset(state.offset.roundToInt(), 0)

    val boxCoverWidth = swipeOffset.x.pxToDp() + squareSize + innerBoxPadding

    val swipeProgress = (state.offset / limitPx).coerceIn(0f, 1f)

    val arrowColor = lerp(arrowStartColor, AppColor.White, swipeProgress)
    val trackColor = lerp(AppColor.White, containerColor, swipeProgress)


    Box(modifier = modifier
        .width(buttonWidth)
        .height(buttonHeight)
        .anchoredDraggable(state = state, orientation = Orientation.Horizontal)
        .clip(RoundedCornerShape(16.dp))
        .background(color = containerColor)) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text, style = AppTypography.bodyXL.copy(color = textColor)
        )

        // this box will cover the text as the swipe action is performed
        Box(modifier = Modifier
            .width(boxCoverWidth)
            .height(buttonHeight)
            .background(color = containerColor))

        // inner box that will be dragged
        Box(
            modifier = Modifier
                .padding(start = innerBoxPadding, end = innerBoxPadding)
                .align(Alignment.CenterStart)
        ) {

            Box(modifier = Modifier
                .size(squareSize)
                .offset { IntOffset(x= state.requireOffset().toInt(), y = 0) }
                .clip(RoundedCornerShape(10.dp))
                .background(color = trackColor.copy(alpha = transparency))) {

                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(24.dp)
                        .height(24.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    tint = arrowColor,
                    contentDescription = "arrow right"
                )
            }
        }
    }
}

@Composable
private fun SlidingButtonDisabled(
    text: String,
    modifier: Modifier = Modifier,
    buttonType: SlidingButtonType = SlidingButtonType.Primary,
) {
    val buttonWidth = SlidingButtonDimen.BUTTON_WIDTH
    val buttonHeight = SlidingButtonDimen.BUTTON_HEIGHT
    val squareSize = SlidingButtonDimen.INNER_SQUARE_SIZE
    val innerBoxPadding = SlidingButtonDimen.INNER_PADDING

    val contentColor = when(buttonType) {
        SlidingButtonType.Primary,
        SlidingButtonType.Success -> AppColor.Navy400
        SlidingButtonType.Secondary -> AppColor.Primary
        SlidingButtonType.Critical -> AppColor.Red200
    }

    Box(modifier = modifier
        .width(buttonWidth)
        .height(buttonHeight)
        .clip(RoundedCornerShape(16.dp))
        .background(color = AppColor.Navy200)) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text, style = AppTypography.bodyXL.copy(color = contentColor)
        )

        Box(
            modifier = Modifier
                .padding(start = innerBoxPadding, end = innerBoxPadding)
                .align(Alignment.CenterStart)
        ) {

            Box(modifier = Modifier
                .size(squareSize)
                .background(color = AppColor.Transparent)) {

                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(24.dp)
                        .height(24.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    tint = contentColor,
                    contentDescription = "arrow right"
                )
            }
        }
    }
}

@Composable
private fun SlidingButtonLoading(
    modifier: Modifier = Modifier,
    buttonType: SlidingButtonType = SlidingButtonType.Primary,
) {
    val buttonWidth = 312.dp
    val buttonHeight = 56.dp

    val mainColor = when(buttonType) {
        SlidingButtonType.Primary -> AppColor.Primary
        SlidingButtonType.Secondary -> AppColor.Navy200
        SlidingButtonType.Success -> AppColor.Green
        SlidingButtonType.Critical -> AppColor.Red
    }

    Box(modifier = modifier
        .width(buttonWidth)
        .height(buttonHeight)
        .clip(RoundedCornerShape(16.dp))
        .background(color = mainColor)) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(24.dp),
            color = AppColor.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CriticalSlidingButton(
    text: String,
    enabled: Boolean = true,
    progressLoading: Boolean = false,
    onSlideComplete: () -> Unit,
) {
    SlidingButton(
        text = text,
        buttonType = SlidingButtonType.Critical,
        completeThreshold = CompleteThreshold.Hard,
        enabled = enabled,
        progressLoading = progressLoading,
        onSlideComplete = onSlideComplete
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun SlidingButtonPreview() {
    val context = LocalContext.current
    Column {
        SlidingButton(text = "Slider label") { Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show() }
        Box(modifier = Modifier.height(16.dp))

        SlidingButton(text = "Secondary", buttonType = SlidingButtonType.Secondary) { Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show() }
        Box(modifier = Modifier.height(16.dp))

        SlidingButton(text = "Slider label", buttonType = SlidingButtonType.Success) {}
        Box(modifier = Modifier.height(16.dp))
        CriticalSlidingButton(text = "Danger label") {
            Toast.makeText(context, "Danger", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun SlidingButtonLoadingPreview() {
    val context = LocalContext.current
    Column {
        SlidingButton(text = "Slider label", progressLoading = true) { Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show() }
        Box(modifier = Modifier.height(16.dp))

        SlidingButton(text = "Slider label", buttonType = SlidingButtonType.Success, progressLoading = true) {}
        Box(modifier = Modifier.height(16.dp))

        CriticalSlidingButton(text = "Danger label", progressLoading = true) {
            Toast.makeText(context, "Danger", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun SlidingButtonDisabledPreview() {
    val context = LocalContext.current
    Column {
        SlidingButton(text = "Slider label", enabled = false) { Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show() }
        Box(modifier = Modifier.height(16.dp))

        SlidingButton(text = "Slider label", buttonType = SlidingButtonType.Success, enabled = false) {}
        Box(modifier = Modifier.height(16.dp))

        CriticalSlidingButton(text = "Danger label", enabled = false) {
            Toast.makeText(context, "Danger", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }