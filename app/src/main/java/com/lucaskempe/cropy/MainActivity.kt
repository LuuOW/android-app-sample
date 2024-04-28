package com.lucaskempe.cropy

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
        startService(intent)

        val modifier = Modifier

        setContent {

            val navController = rememberNavController()
            val vm = UsersViewModel(navController)

            CropyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val bottomBarRoutes = listOf("home")
                    val showTopBar = remember { mutableStateOf(false) }

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    LaunchedEffect(navController) {
                        navController.currentBackStackEntryFlow.collect { backStackEntry ->
                            val currentRoute = backStackEntry.destination.route
                            showTopBar.value = currentRoute in bottomBarRoutes
                        }
                    }

                    Scaffold(
                        modifier = modifier,
                        drawerContent =  {
                            CustomDrawer(activityResultLauncher, modifier = modifier, drawerState= drawerState)
                        },
                        topBar = {
                            if (showTopBar.value) CustomSearch(vm = vm)
                        },
                        bottomBar = {
                            Box {
                                BottomBar(
                                    context = LocalContext.current,
                                    navController = navController,
                                    items = listOf(BottomBarItem.Home, BottomBarItem.Build),
                                    modifier = modifier
                                )
                                Canvas(
                                    modifier = modifier
                                        .clickable {

                                        }
                                        .align(Alignment.Center)
                                        .size(35.dp)
                                        .padding(bottom = 20.dp)
                                        .windowInsetsPadding(WindowInsets.displayCutout)
                                ) {
                                    drawCircle(
                                        color = Color(70,156,221),
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

}