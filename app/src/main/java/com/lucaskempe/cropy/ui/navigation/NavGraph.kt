package com.lucaskempe.cropy.ui.navigation

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
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
    navController: NavController,
    modifier: Modifier,
    activityResultLauncher: ActivityResultLauncher<Array<String>>,
    context: Context,
    vm: UsersViewModel
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "home",
        route = "home_graph"
    ) {


        composable("home") { HomeScreen(modifier, navController, vm = vm) }

        navigation(
            startDestination = "user_profile/{userId}",
            route = "user_graph",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {

            composable(route = "user_profile/{userId}") { backStack ->
                backStack.arguments?.getString("userId")
                    ?.let { userId ->
                        vm.userId.intValue = userId.toInt()
                        UserCardScreen(
                            navController = navController,
                            vm = vm,
                            userId = userId.toInt()
                        )
                    }
            }

            navigation(
                startDestination = "map",
                route = "map_graph"
            ) {

                composable(route = "map") {
                    MapScreen(modifier, navController, context, vm = vm)
                }
            }


            composable("build") {
                BuildScreen(
                    navController = navController,
                    vm = vm,
                    modifier = modifier
                )
            }

            navigation(startDestination = "inspector", route = "inspector_graph") {

                composable("inspector") {
                    InspectorsScreen(
                        navController = navController,
                        activityResultLauncher = activityResultLauncher,
                        modifier = modifier,
                        context = context
                    )
                }

                navigation(
                    startDestination = "inspector/image_capture",
                    route = "image_capture_graph"
                ) {
                    composable("inspector/image_capture") { CameraXPreview() }
                }

            }

        }



        }




}
