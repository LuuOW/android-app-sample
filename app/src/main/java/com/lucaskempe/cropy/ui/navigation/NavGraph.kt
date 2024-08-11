package com.lucaskempe.cropy.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.viewmodels.MapsViewModel
import com.lucaskempe.cropy.ui.authentication.AuthViewmodel
import com.lucaskempe.cropy.ui.navigation.api.DestinationGraph
import com.lucaskempe.cropy.viewmodels.ChatViewModel


@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    session: Boolean,
    authViewmodel: AuthViewmodel,
    mapsViewModel: MapsViewModel,
    chatViewModel: ChatViewModel,
    navController: NavHostController,
    padding: PaddingValues
) {
    DestinationGraph(session,authViewmodel,mapsViewModel,chatViewModel ,navController, padding)


 /*   NavHost(
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

    }*/

}
