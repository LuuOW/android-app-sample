package com.lucaskempe.cropy.ui.navigation.api

import android.app.Activity
import android.content.Context
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.lucaskempe.cropy.ui.authentication.AuthViewmodel
import com.lucaskempe.cropy.ui.authentication.LoginScreen
import com.lucaskempe.cropy.ui.authentication.CodeVerificationScreen
import com.lucaskempe.cropy.ui.authentication.MailRequestScreen
import com.lucaskempe.cropy.ui.authentication.ResetPasswordScreen
import com.lucaskempe.cropy.ui.authentication.SignUpScreen
import com.lucaskempe.cropy.ui.authentication.WelcomeScreen
import com.lucaskempe.cropy.ui.cards.UserCardScreen
import com.lucaskempe.cropy.ui.image.CameraXPreview
import com.lucaskempe.cropy.ui.screens.BuildScreen
import com.lucaskempe.cropy.ui.screens.chat.ChatScreen
import com.lucaskempe.cropy.ui.screens.HomeScreen
import com.lucaskempe.cropy.ui.screens.InspectorsScreen
import com.lucaskempe.cropy.ui.screens.MapScreen
import com.lucaskempe.cropy.viewmodels.UsersViewModel

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

    val authVm: AuthViewmodel = hiltViewModel()

    val router: RouterDestinity = RouterImplementation(nvm.destSet)

    router.createGraph(
        "auth_graph",
        "welcome",
        mutableMapOf(
            "welcome" to { WelcomeScreen(navController) },
            "login" to { LoginScreen(navController, vm = authVm) },
            "sign_up" to { SignUpScreen(context, activity, navController, vm=authVm) },
            "verification" to { CodeVerificationScreen(navController, authVm) },
            "reset" to { ResetPasswordScreen(navController,authVm) },
            "mail_request" to { MailRequestScreen(navController = navController, vm = authVm) }
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

    router.createGraph(
        graphName = "invisible_graph",
        startGraphDestiny = "invisible",
        destinationList = mutableMapOf(
            "invisible" to { HomeApp(navController) }
        )
    )

    DestinationHolder(nvm.destSet, navController, auth)

}

@Composable
fun HomeApp(navController: NavHostController) {
    val context = LocalContext.current
    val localDensity = LocalDensity.current
    val modifier = Modifier
    val gridState = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        state = gridState,
        columns = StaggeredGridCells.Adaptive(60.dp + (35*2/localDensity.density).dp)) {
        itemsIndexed(myIconList) { index, data ->
            IconItemBox(
                itemData = data,
                modifier,
                onAnimationFinish = {
                    myIconList.dropLast(0)
                },
                onClick = {
                    if (!data.isEmpty.value && data.isEnabled.value) {
                        data.isEnabled.value = false
                        data.offset.value = gridState.layoutInfo.visibleItemsInfo.last().offset
                        myIconList.filterNot { it.id == 16 }.forEachIndexed { index, iconItem ->
                            if (iconItem.isEmpty.value) {
                                //iconItem.offset.value = gridState.layoutInfo.visibleItemsInfo.last().offset
                            }
                        }
                    } else if (!data.isEmpty.value && !data.isEnabled.value) {
                        data.isEnabled.value = true
                        data.offset.value = gridState.layoutInfo.visibleItemsInfo.first().offset
                    }
                }
            )
        }

    }
}

@Composable
fun IconItemBox(
    itemData: IconItem,
    modifier: Modifier,
    onClick: () -> Unit = {},
    onAnimationFinish: () -> Unit = { },
    onPosition: (IntOffset, Int) -> Unit = { offSet, iD -> },
) {
    val strokeColor = when {
        itemData.isEmpty.value && !itemData.isEnabled.value -> Color.Gray  // Empty & Disabled: Gray outline
        !itemData.isEmpty.value && !itemData.isEnabled.value -> Color.Red // Non-empty & Disabled: Red outline
        else -> Color.Green                              // No outline (default)
    }

    val offset by animateIntOffsetAsState(
        animationSpec = tween(durationMillis = 700, delayMillis = 100),
        targetValue = itemData.offset.value,
        label = "offset",
        finishedListener = { onAnimationFinish() }
    )

    Box(
        modifier = modifier
            .offset { offset }
            .padding(60.dp)
            .clickable() {
                onClick()
            }
            .onGloballyPositioned {
                onPosition(
                    it
                        .positionInRoot()
                        .round(), itemData.id
                )
            }

    ){
        androidx.compose.foundation.Canvas(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .align(Alignment.Center)
        ) {
            drawCircle(
                color = strokeColor,
                radius = 35.dp.toPx(),
                style = Stroke(width = 3.dp.toPx())
            )
        }

    }
}

data class IconItem(
    val id: Int,
    var isEmpty: MutableState<Boolean>,
    var isEnabled: MutableState<Boolean>,
    var offset: MutableState<IntOffset> = mutableStateOf(IntOffset.Zero)
)

fun createIconList(): MutableList<IconItem> {
    val iconList = mutableListOf<IconItem>()

    // Add 1 IconItem that is not empty and enabled
    iconList.add(
        IconItem(id = 1, isEmpty = mutableStateOf(false), isEnabled = mutableStateOf(true))
    )

    // Add 7 IconItems that are empty and disabled
    for (i in 2..8) {
        iconList.add(
            IconItem(id = i, isEmpty = mutableStateOf(true), isEnabled = mutableStateOf(false))
        )
    }

    // Add 7 IconItems that are not empty and disabled
    for (i in 9..16) {
        iconList.add(
            if (i != 16) {
                IconItem(id = i, isEmpty = mutableStateOf(false), isEnabled = mutableStateOf(false))
            } else {
                IconItem(id = i, isEmpty = mutableStateOf(true), isEnabled = mutableStateOf(false))
            }
        )
    }

    return iconList
}

// Usage example:
val myIconList = createIconList()