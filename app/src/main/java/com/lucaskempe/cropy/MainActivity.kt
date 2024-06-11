package com.lucaskempe.cropy

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.lucaskempe.cropy.services.AppService
import com.lucaskempe.cropy.ui.cards.ButtonSlider
import com.lucaskempe.cropy.ui.navigation.BottomBar
import com.lucaskempe.cropy.ui.navigation.BottomBarItem
import com.lucaskempe.cropy.ui.navigation.NavGraph
import com.lucaskempe.cropy.ui.theme.CropyTheme
import com.lucaskempe.cropy.ui.views.CustomDrawer
import com.lucaskempe.cropy.ui.views.CustomSearch
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(context)    
        val intent = Intent(this, AppService::class.java)
        val modifier = Modifier

        installSplashScreen()

        setContent {
            val vm: UsersViewModel = hiltViewModel()
            val navController: NavHostController = rememberNavController()

            startService(intent)

            vm.isImageSelected.apply {
                intent.putExtra("imageUri", vm.selectedImageUri)
            }

            CropyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier.fillMaxSize(),
                ) {
                    val searchBarRoutes = listOf("home")
                    val bottomBarItems = listOf(
                        BottomBarItem.Home,
                        BottomBarItem.Inspectors,
                        BottomBarItem.Invisible,
                        BottomBarItem.Build,
                        BottomBarItem.Chat
                    )
                    val bottomBarRoutes = listOf(
                        "home",
                        "inspector",
                        "build"
                    )
                    val showTopBar = remember { mutableStateOf(false) }
                    val showBottomBar = remember { mutableStateOf(false) }
                    val auth = FirebaseAuth.getInstance()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    LaunchedEffect(navController) {
                        navController.currentBackStackEntryFlow.collect { backStackEntry ->
                            val currentRoute = backStackEntry.destination.route
                            if (auth.currentUser != null) {
                                showTopBar.value = currentRoute in searchBarRoutes
                                showBottomBar.value = true
                            }
                        }
                    }

                    Scaffold(
                        modifier = modifier,
                        drawerContent = {
                            CustomDrawer(
                                modifier = modifier,
                                drawerState = drawerState,
                                auth=auth
                            )
                        },
                        topBar = {
                            if (showTopBar.value) {
                                Column {
                                    CustomSearch(vm = vm)
                                    ButtonSlider()
                                }
                            }
                        },
                        bottomBar = {

                            if (showBottomBar.value) {
                                Box(
                                ) {
                                    BottomBar(
                                        context = context,
                                        navController = navController,
                                        items = bottomBarItems,
                                        modifier = modifier
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
                            activity = this,
                            padding = innerPadding,
                            modifier = Modifier
                                .padding(innerPadding),
                            navController = navController,
                            context = this,
                            auth = auth
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