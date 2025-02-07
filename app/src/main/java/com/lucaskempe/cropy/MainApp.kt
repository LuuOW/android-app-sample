package com.lucaskempe.cropy

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.lucaskempe.cropy.ui.authentication.AuthViewmodel
import com.lucaskempe.cropy.ui.cards.ChipSlider
import com.lucaskempe.cropy.ui.navigation.BottomBar
import com.lucaskempe.cropy.ui.navigation.BottomBarItem
import com.lucaskempe.cropy.ui.navigation.NavGraph
import com.lucaskempe.cropy.ui.theme.CropyTheme
import com.lucaskempe.cropy.ui.views.CustomDrawer
import com.lucaskempe.cropy.ui.views.CustomSearch
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import com.lucaskempe.cropy.viewmodels.MapsViewModel
import com.lucaskempe.cropy.viewmodels.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun MainApp(
    session: Boolean,
    viewModel: UsersViewModel,
    authViewmodel: AuthViewmodel,
    mapsViewModel: MapsViewModel,
    chatViewModel: ChatViewModel
) {

    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current

    CropyTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            val searchBarRoutes = listOf("home")
            val bottomBarItems = listOf(
                BottomBarItem.Home,
                BottomBarItem.Inspectors,
                BottomBarItem.Invisible,
                BottomBarItem.Build,
                BottomBarItem.Chat
            )
            val bottomBarRoutes =  listOf(
                "home",
                "inspector",
                "build"
            )

            val showTopBar = remember { mutableStateOf(false) }
            val showBottomBar = remember { mutableStateOf(false) }
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            LaunchedEffect(navController, context) {

                navController.currentBackStackEntryFlow.collect { backStackEntry ->
                    val currentRoute = backStackEntry.destination.route
                    val currentGraphId = backStackEntry.destination.parent?.route
                        ?: backStackEntry.destination.id // Get parent graph ID or destination ID if no parent
                    if (currentGraphId != "auth_graph") {
                        showTopBar.value = currentRoute in searchBarRoutes
                        showBottomBar.value = true
                    }
                }
            }

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = { CustomDrawer(
                    modifier = Modifier,
                    drawerState = drawerState,
                    context = context,
                    navController = navController,
                    showTopBar = showTopBar,
                    showBottomBar = showBottomBar
                ) },
                content = {
                    Scaffold(
                        modifier = Modifier,
                        topBar = {
                            if (showTopBar.value) {
                                Column {
                                    CustomSearch(vm = viewModel, drawerState)
                                    ChipSlider()
                                }
                            }
                        },
                        bottomBar = {

                            if (showBottomBar.value) {
                                Box(
                                ) {
                                    BottomBar(
                                        context = viewModel.context,
                                        navController = navController,
                                        items = bottomBarItems,
                                        modifier = Modifier
                                    )


                                    val shapeA = remember {
                                        RoundedPolygon(
                                            12,
                                            rounding = CornerRounding(0.2f)
                                        )
                                    }
                                    val shapeB = remember {
                                        RoundedPolygon.star(
                                            12,
                                            rounding = CornerRounding(0.2f)
                                        )
                                    }
                                    val morph = remember {
                                        Morph(shapeA, shapeB)
                                    }
                                    val infiniteTransition =
                                        rememberInfiniteTransition("infinite outline movement")
                                    val animatedProgress = infiniteTransition.animateFloat(
                                        initialValue = 0f,
                                        targetValue = 1f,
                                        animationSpec = infiniteRepeatable(
                                            tween(5000, easing = LinearEasing),
                                            repeatMode = RepeatMode.Reverse
                                        ),
                                        label = "animatedMorphProgress"
                                    )
                                    val animatedRotation = infiniteTransition.animateFloat(
                                        initialValue = 0f,
                                        targetValue = 360f,
                                        animationSpec = infiniteRepeatable(
                                            tween(6000, easing = LinearEasing),
                                            repeatMode = RepeatMode.Reverse
                                        ),
                                        label = "animatedMorphProgress"
                                    )

                                    val imageDrawable: MutableState<Bitmap> = remember {
                                        mutableStateOf(
                                            Bitmap.createBitmap(
                                                1,
                                                1,
                                                Bitmap.Config.ARGB_8888
                                            )
                                        )
                                    }

                                    val imageLoader =
                                        ImageLoader(context = viewModel.context).newBuilder().build()
                                    val request = ImageRequest.Builder(context = viewModel.context)
                                        .data("https://i.pinimg.com/474x/80/b8/93/80b893e3c9fbbc26d89f0c275f0967e7.jpg")
                                        .crossfade(true)
                                        .build()

                                    LaunchedEffect(
                                        key1 = viewModel.context,
                                    ) {
                                        val result = imageLoader.execute(request).drawable
                                        imageDrawable.value = Bitmap.createScaledBitmap(
                                            (result as BitmapDrawable).bitmap,
                                            300,
                                            300,
                                            false
                                        )
                                    }

                                    Canvas(
                                        modifier = Modifier
                                            .padding(
                                                start = 30.dp,
                                                end = 30.dp,
                                                bottom = 18.dp
                                            )
                                            .size(35.dp)
                                            /*                                            .clip(
                                                CustomRotatingMorphShape(
                                                    morph,
                                                    animatedProgress.value,
                                                    animatedRotation.value
                                                )
                                            )*/
                                            .windowInsetsPadding(WindowInsets.displayCutout)
                                            .align(Alignment.Center)
                                            .clickable {
                                                navController.navigate("invisible_graph") {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                        inclusive = false
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                    ) {
                                        //drawImage(image = imageDrawable.value.asImageBitmap())
                                        drawCircle(
                                            color = Color(70, 156, 221),
                                            radius = 35.dp.toPx()
                                        )
                                    }
                                }
                            }

                        }
                    ) { innerPadding ->
                        NavGraph(
                            session = session,
                            authViewmodel = authViewmodel,
                            chatViewModel = chatViewModel,
                            padding = innerPadding,
                            navController = navController,
                            mapsViewModel = mapsViewModel
                        )
                    }
                }
            )
        }

    }
}