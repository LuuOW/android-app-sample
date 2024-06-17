package com.lucaskempe.cropy.ui.navigation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.material.bottomappbar.BottomAppBar

@Composable
fun BottomBar(
    navController: NavController, // Get NavController from your composable hierarchy
    items: List<BottomBarItem>,
    context: Context,
    modifier: Modifier
) {
    BottomNavigation(

    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            var selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            BottomNavigationItem(
                enabled = if (currentDestination?.route != "invisible_graph") true else false,
                modifier = modifier
                    .align(Alignment.CenterVertically),
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = null,
                        modifier = modifier
                    )
                },
                alwaysShowLabel = true,
                label = { screen.title },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {

                    navController.navigate(screen.route) {
                        restoreState = true
                        launchSingleTop = true
                    }

                }

            )
        }
    }
}

