package com.lucaskempe.cropy.ui.navigation.api

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.cropy.datastore.user.UserSessionDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DestinationHolder(
    session: Boolean,
    destinationSet: MutableSet<DestinationObject>,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = if(session) "home_graph" else "auth_graph",
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