package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.lucaskempe.cropy.viewmodels.UsersViewModel


@Composable fun ButtonGridComposable() {
    val navController = rememberNavController()
    val viewModel = UsersViewModel(navController)
    val buttonList =viewModel.fIconList
}