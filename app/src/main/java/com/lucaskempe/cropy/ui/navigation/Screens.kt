package com.lucaskempe.cropy.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Map : Screen("map")
    object Inspectors : Screen("inspectors")
    object ImageSelection: Screen("image_selection")
    object UserProfile: Screen("user_profile")
    object CameraX: Screen("image_capture")
}
