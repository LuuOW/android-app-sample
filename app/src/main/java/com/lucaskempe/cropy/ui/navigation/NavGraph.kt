package com.lucaskempe.cropy.ui.navigation

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.lucaskempe.cropy.ui.cards.UserCardScreen
import com.lucaskempe.cropy.ui.image.CameraXPreview
import com.lucaskempe.cropy.ui.screens.BuildScreen
import com.lucaskempe.cropy.ui.screens.HomeScreen
import com.lucaskempe.cropy.ui.screens.InspectorsScreen
import com.lucaskempe.cropy.ui.screens.MapScreen
import com.lucaskempe.cropy.viewmodels.UsersViewModel


@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
    context: Context,
    vm: UsersViewModel
) {

    NavHost(
        navController = navController,
        startDestination = "home_graph"
    ) {

        navigation(
            startDestination = "home",
            route = "home_graph"
        ) {

            composable("home") {
                HomeScreen(
                    //modifier = modifier,
                    navController = navController,
                    vm = vm,
                    onClick = {
                        navController.navigate("user_profile")
                    }
                )
            }
            composable(route = "user_profile") {
                UserCardScreen(
                    vm = vm,
                    navController = navController,
                    onClick = {
                        navController.navigate("map")
                    }
                )
            }
            composable(route = "map") {
                MapScreen(
                    modifier,
                    navController,
                    context,
                    vm = vm,
                    onClick = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive=true }
                        }
                    }
                )
            }
        }
        // Inspector

        navigation(
            startDestination = "inspector",
            route = "inspector_graph"
        ) {
            composable("inspector") {
                InspectorsScreen(
                    navController = navController,
                    //modifier = modifier,
                    context = context,
                    vm=vm,
                    onClick = { navController.navigate("image_capture") }
                )
            }
            composable("image_capture") { CameraXPreview(context) }
        }

        navigation(
            startDestination = "build",
            route = "build_graph"
        ) {
            composable("build") {
                BuildScreen(
                    navController = navController,
                    vm = vm,
                    modifier = modifier
                )
            }
        }

    }

}
