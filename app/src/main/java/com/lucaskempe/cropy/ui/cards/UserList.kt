package com.lucaskempe.cropy.ui.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.ui.navigateUp
import androidx.room.util.query
import coil.compose.AsyncImage
import com.lucaskempe.cropy.services.models.User
import com.lucaskempe.cropy.viewmodels.UsersViewModel

@Composable
fun UserList(
    users: List<User>,
    navigationController: NavController,
    vm: UsersViewModel
    ) {

    vm.fetchUsers()


    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(users.size) { index ->
            if (users[index].address.contains(vm.userQuery.value, ignoreCase = true)) {
                UserListItem(user = users[index], navigationController = navigationController)
            }
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    navigationController: NavController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { // Handle card click
                // navigate to the user's profile with the user entire user object
                navigationController.navigate("user_profile/${user.id}") {
                    // Pop up to the start destination of the user graph to
                    // avoid building up a large stack of destinations
                    popUpTo(navigationController.graph.findStartDestination().id) {
                        saveState = true
                        inclusive=true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true

                }
            }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Image
            AsyncImage(
                model = user.image, // Use user.image URL directly
                contentDescription = "Profile picture of ${user.businessName}",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape) // Make image circular
            )
            Spacer(modifier = Modifier.width(8.dp))

            // Name
            Text(text = user.businessName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}