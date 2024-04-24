package com.lucaskempe.cropy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BuildScreem(
    navController: NavHostController
) {




    // row and column of 5x2 items with rounded form (like avatars use async image)
    // the items must be clickable

    val modifier = Modifier
    val context = LocalContext.current
    val imageURLForItems = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png"

// row of 5 items

    Row(modifier = modifier) {
        Column {
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
            DropBox(navController = navController)
        }
        Column {
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
        }
        Column {
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
        }
        Column {
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
        }
        Column {
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
            CustomButton(navController=navController, dragState = remember { mutableStateOf(DragState.NOT_DRAGGING) })
        }
    }

}

enum class DragState {
    DRAGGING,
    NOT_DRAGGING
}

enum class DropState {
    DROPPED,
    NOT_DROPPED
}

@ExperimentalFoundationApi
@Composable
fun DropBox(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val dropState = remember { mutableStateOf(DropState.NOT_DROPPED) }

    Box(
        modifier = Modifier
            .size(100.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
            .dragAndDropSource(
                drawDragDecoration = {

                }
            ) {



            }
    ) { /* Content of the draggable component*/ }
}

@Composable
private fun CustomButton(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    dragState: MutableState<DragState>
) {
    val imageURLForItems = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png"
    val context = LocalContext.current

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (dragState.value == DragState.DRAGGING) Color.Transparent else Color.White,
        ),
        shape = AbsoluteRoundedCornerShape(24.dp),
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .draggable(
                onDragStarted = {
                    dragState.value = DragState.DRAGGING
                },
                onDragStopped = {
                    dragState.value = DragState.NOT_DRAGGING
                },
                orientation = Orientation.Horizontal,
                state = rememberDraggableState {
                    offsetX += it
                }
            )
            .draggable(
                onDragStarted = {
                    dragState.value = DragState.DRAGGING
                },
                onDragStopped = {
                    dragState.value = DragState.NOT_DRAGGING
                },
                orientation = Orientation.Vertical,
                state = rememberDraggableState {
                    offsetY += it
                }
            )
            .padding(8.dp)
    ) {
        IconButton(
            modifier = modifier
                .background(Color.Transparent)
                .size(80.dp),
            onClick = {
                navController.navigate("home")
            }) {
            AsyncImage(model = imageURLForItems,
                contentDescription = "Icon}",
                modifier = modifier
                    .background(Color.Transparent)
                    .alpha(
                        if (dragState.value == DragState.DRAGGING) 0.5f else 1f
                    )
                    .size(60.dp)
                    .clip(CircleShape) )
        }
    }
}

