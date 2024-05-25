package com.lucaskempe.cropy.ui.navigation.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DestinationHolder(
    destinationSet: MutableSet<DestinationObject>,
    navController: NavHostController,
    auth: FirebaseAuth
) {

    NavHost(
        navController = navController,
        startDestination = if(auth.currentUser != null) "build_graph" else "auth_graph",
    ) {
        destinationSet.forEach { graph ->
            navigation(
                startDestination = graph.startGraphDestiny,
                route = graph.graphName
            ) {
                graph.composables.keys.forEach {route ->
                    composable(route) {
                        graph.composables[route]?.invoke()
                    }
                }

            }

        }

    }

}