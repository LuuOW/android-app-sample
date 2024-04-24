package com.lucaskempe.cropy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.lucaskempe.cropy.R

sealed class BottomBarItem(val icon: ImageVector, val title: String, val route: String) {
    object Home : BottomBarItem(Icons.Filled.Home, "Home", "home")
    object Map: BottomBarItem(Icons.Filled.LocationOn, "Map", "map")
    object Inspectors : BottomBarItem(Icons.Filled.Person, "Inspectors", "inspector")

    object Build : BottomBarItem(Icons.Filled.Build, "Build", "build")
    // Add more items here
}