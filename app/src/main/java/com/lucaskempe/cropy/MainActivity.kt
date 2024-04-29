package com.lucaskempe.cropy

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.lucaskempe.cropy.services.AppService
import com.lucaskempe.cropy.ui.navigation.BottomBar
import com.lucaskempe.cropy.ui.navigation.BottomBarItem
import com.lucaskempe.cropy.ui.navigation.NavGraph
import com.lucaskempe.cropy.ui.theme.CropyTheme
import com.lucaskempe.cropy.ui.views.CustomDrawer
import com.lucaskempe.cropy.ui.views.CustomSearch
import com.lucaskempe.cropy.viewmodels.UsersViewModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    private val REQUIRED_PERMISSIONS = mapOf(
        "CAMERA" to false,
        "ACCESS_COARSE_LOCATION" to false,
        "ACCESS_FINE_LOCATION" to false,
        "POST_NOTIFICATIONS" to false
    )

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

            }
        }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, AppService::class.java)
        intent.putExtra("dataKey", "dataValue")
        //startService(intent)

        val modifier = Modifier
        val context = this@MainActivity

        setContent {

            val navController = rememberNavController()
            val vm = UsersViewModel(navController)

            CropyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier.fillMaxSize(),
                    color = Color(0xFFD0BCFF)
                ) {
                    val searchBarRoutes = listOf("home")
                    val bottomBarRoutes = listOf("home", "inspectors", "build")
                    val showTopBar = remember { mutableStateOf(false) }
                    val showBottomBar = remember { mutableStateOf(false) }

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    LaunchedEffect(navController) {
                        navController.currentBackStackEntryFlow.collect { backStackEntry ->
                            val currentRoute = backStackEntry.destination.route
                            showTopBar.value = currentRoute in searchBarRoutes
                            showBottomBar.value = currentRoute in bottomBarRoutes
                        }
                    }

                    Scaffold(
                        modifier = modifier,
                        drawerContent = {
                            CustomDrawer(
                                activityResultLauncher,
                                modifier = modifier,
                                drawerState = drawerState
                            )
                        },
                        topBar = {
                            if (showTopBar.value) CustomSearch(vm = vm)
                        },
                        bottomBar =  {
                            if (showBottomBar.value)
                            Box(
                            ) {
                                val items =listOf(BottomBarItem.Home,BottomBarItem.Inspectors, BottomBarItem.Build)
                                BottomBar(
                                    context = context,
                                    navController = navController,
                                    items = items,
                                    modifier = modifier.background(Color(0xFFD0BCFF))
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
                                    ImageLoader(context = context).newBuilder().build()
                                val request = ImageRequest.Builder(context = context)
                                    .data("https://i.pinimg.com/474x/80/b8/93/80b893e3c9fbbc26d89f0c275f0967e7.jpg")
                                    .crossfade(true)
                                    .build()

                                LaunchedEffect(
                                    key1 = context,
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
                                    modifier = modifier
                                        .padding(start = 30.dp, end = 30.dp, bottom = 18.dp)
                                        .size(35.dp)
                                        .windowInsetsPadding(WindowInsets.safeDrawing)
                                        /*.clip(
                                            CustomRotatingMorphShape(
                                                morph,
                                                animatedProgress.value,
                                                animatedRotation.value
                                            )
                                        )*/
                                        .align(Alignment.Center)
                                        .clickable {
                                            navController.navigate(items[1].route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                ) {
                                  /*  drawImage(
                                        image = imageDrawable.value.asImageBitmap()
                                    )*/

                                    drawCircle(
                                        color = Color(70, 156, 221),
                                        radius = 35.dp.toPx()
                                    )
                                }
                            }


                        }
                    ) {
                        NavGraph(
                            modifier = Modifier
                                .padding(it),
                            navController = navController,
                            activityResultLauncher = activityResultLauncher,
                            context = this,
                            vm = vm
                        )
                    }

                }

            }
        }
    }


    class CustomRotatingMorphShape(
        private val morph: Morph,
        private val percentage: Float,
        private val rotation: Float
    ) : Shape {

        private val matrix = androidx.compose.ui.graphics.Matrix()
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            // Below assumes that you haven't changed the default radius of 1f, nor the centerX and centerY of 0f
            // By default this stretches the path to the size of the container, if you don't want stretching, use the same size.width for both x and y.
            matrix.scale(size.width / 1.8f, size.height / 1.8f)
            matrix.translate(1f, 1f)
            matrix.rotateZ(rotation)

            val path = morph.toPath(progress = percentage).asComposePath()
            path.transform(matrix)

            return Outline.Generic(path)
        }
    }


}