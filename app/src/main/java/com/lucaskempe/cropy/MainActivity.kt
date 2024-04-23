package com.lucaskempe.cropy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, AppService::class.java)
        intent.putExtra("dataKey", "dataValue")
        startService(intent)

        setContent {

            val navController = rememberNavController()
            val vm = UsersViewModel()

            CropyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val bottomBarRoutes = listOf("home")
                    val showTopBar = remember { mutableStateOf(false) }

                    LaunchedEffect(navController) {
                        navController.currentBackStackEntryFlow.collect { backStackEntry ->
                            val currentRoute = backStackEntry.destination.route
                            showTopBar.value = currentRoute in bottomBarRoutes
                        }
                    }

                    Scaffold(
                        drawerContent =  {
                            CustomDrawer(activityResultLauncher)
                        },
                        topBar = {
                            if (showTopBar.value) CustomSearch(vm = vm)
                        },
                        bottomBar = {
                            BottomBar(
                                navController = navController,
                                items = listOf(
                                    BottomBarItem.Home,
                                    BottomBarItem.Inspectors
                                )
                            )
                        }
                    ) {
                        NavGraph(
                            modifier = Modifier.padding(it),
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