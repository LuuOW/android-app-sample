package com.lucaskempe.cropy.ui.navigation.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun InfoScreen(
    navController: NavController
) {
    Column {
        Text("Welcome to info graph")

        Row {
            Button(onClick = {
                navController.navigate("name")
            }) {
                Text(text = "navigate name")
            }

            Button(onClick = {
                navController.navigate("settings_graph")
            }) {
                Text(text = "navigate settings graph")
            }
        }


    }

}

@Composable
fun SettingsScreen(
    navController: NavController
) {
    Column {
        Text("Welcome to settings Graph")
        Row {
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "navigate back")
            }
            Button(onClick = {
                navController.navigate("change_password")
            }) {
                Text(text = "navigate to change password")
            }
        }
    }
}

@Composable
fun ChangeSettings(
    navController: NavController
) {
    Column {
        Text("You're still in settings Graph")
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text(text = "navigate back")
        }
    }
}

@Composable
fun NameOnScreen(
    navController: NavController,
) {
    Column {
        Text("Jane on Air")
        Button(onClick = {
            navController.navigate("stats")
        }) {
            Text(text = "navigate stats")
        }
    }
}

@Composable
fun Stats(
    navController: NavController
) {
    Text("These are your stats: ")
}