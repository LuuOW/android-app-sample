package com.lucaskempe.cropy.ui.navigation.api

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.drawable.shapes.Shape
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.compose.DragState
import com.lucaskempe.cropy.ui.authentication.SignUpScreen
import com.lucaskempe.cropy.ui.cards.UserCardScreen
import com.lucaskempe.cropy.ui.image.CameraXPreview
import com.lucaskempe.cropy.ui.screens.BuildScreen
import com.lucaskempe.cropy.ui.screens.ChatScreen
import com.lucaskempe.cropy.ui.screens.HomeScreen
import com.lucaskempe.cropy.ui.screens.InspectorsScreen
import com.lucaskempe.cropy.ui.screens.MapScreen
import com.lucaskempe.cropy.ui.screens.items.ButtonData
import com.lucaskempe.cropy.ui.screens.items.CustomButton
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlin.math.roundToInt

@Composable
@ExperimentalMaterial3Api
fun DestinationGraph(
    activity: Activity,
    modifier: Modifier,
    context: Context,
    auth: FirebaseAuth,
    navController: NavHostController,
    padding: PaddingValues
) {

    val nvm: NavigationViewModel = hiltViewModel()

    val vm: UsersViewModel = hiltViewModel()

    val router: RouterDestinity = RouterImplementation(nvm.destSet)

    router.createGraph(
        "auth_graph",
        "sign_up",
        mutableMapOf(
            "sign_up" to { SignUpScreen(context, activity, navController) },
        )
    )
    router.createGraph(
        "home_graph",
        "home",
        mutableMapOf(
            "home" to {
                HomeScreen(
                    navController,
                    vm,
                    onClick = { navController.navigate("user_profile") })
            },
            "user_profile" to {
                UserCardScreen(
                    vm,
                    navController,
                    onClick = { navController.navigate("map") })
            },
            "map" to { MapScreen(modifier, navController, context, vm) }
        )
    )

    router.createGraph(
        "inspector_graph",
        "inspector",
        mutableMapOf(
            "inspector" to { InspectorsScreen(navController, vm) },
            "image_capture" to { CameraXPreview(context) }
        )
    )

    router.createGraph(
        graphName = "build_graph",
        startGraphDestiny = "build",
        destinationList = mutableMapOf(
            //"build" to { HomeApp() }
            "build" to { BuildScreen(navController, vm, modifier) },
        )
    )

    router.createGraph(
        graphName = "chat_graph",
        startGraphDestiny = "chat",
        destinationList = mutableMapOf(
            "chat" to { ChatScreen(navController, padding) }
        )
    )

    DestinationHolder(nvm.destSet, navController, auth)

}