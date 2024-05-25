package com.lucaskempe.cropy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerRoutes(val route: String, val title: String, val icon: ImageVector) {
    object Info : DrawerRoutes("info", "Info", Icons.Filled.Info)
    object Settings : DrawerRoutes("settings", "Settings", Icons.Filled.Settings)
    object Account: DrawerRoutes("account", "My Account", Icons.Filled.AccountBox)
}