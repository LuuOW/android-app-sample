package com.lucaskempe.cropy.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucaskempe.cropy.services.models.users
import com.lucaskempe.cropy.ui.cards.UserCardScreen
import com.lucaskempe.cropy.ui.cards.UserList
import com.lucaskempe.cropy.viewmodels.UsersViewModel

@Composable
fun HomeScreen(modifier: Modifier, navController: NavController, vm: UsersViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 38.dp)
    ) {
        UserList(users = users, navController, vm = vm)
    }
}