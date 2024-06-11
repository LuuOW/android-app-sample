package com.lucaskempe.cropy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.lucaskempe.cropy.R

sealed class BottomBarItem(val icon: ImageVector, val title: String, val route: String) {
    data object Home : BottomBarItem(Icons.Filled.Home, "Home", "home_graph")
    data object Inspectors : BottomBarItem(Icons.Filled.Person, "Inspector", "inspector_graph")
    data object Invisible: BottomBarItem(Icons.Filled.Star, "Invisible", "invisible_graph")
    data object Build : BottomBarItem(Icons.Filled.Build, "Build", "build_graph")
    data object Chat: BottomBarItem(Icons.Filled.MailOutline, "Chat", "chat_graph")
}