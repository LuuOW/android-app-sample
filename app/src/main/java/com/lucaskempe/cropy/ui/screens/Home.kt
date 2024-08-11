package com.lucaskempe.cropy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import coil.compose.AsyncImage
import com.cropy.entities.user.users
import com.lucaskempe.cropy.userHasLocationPermission
import com.lucaskempe.cropy.viewmodels.UsersViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    //modifier: Modifier,
    navController: NavController,
    vm: UsersViewModel,
    onClick: () -> Unit = { }
) {

    val locationWorkReq = OneTimeWorkRequestBuilder<com.cropy.workers.LocationWorker>()
        .addTag("LOCATION_WORKER_TAG")
        .build()



    LaunchedEffect(Unit){
        if(userHasLocationPermission(vm.context)) {
            vm.workManager.enqueueUniqueWork("LOCATION_WORKER_TAG", ExistingWorkPolicy.REPLACE, locationWorkReq)
        }
    }

    val modifier = Modifier
    LazyColumn(modifier = modifier) {
        items(users.size) { index ->
            if (users[index].address.contains(vm.userQuery.text, ignoreCase = true)) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(16.dp)
                        .clickable { // Handle card click
                            vm.loadUserProfile(users[index].id)
                            onClick()
                        },
                    elevation = CardDefaults.cardElevation(15.dp)
                ) {
                    Box(
                        modifier
                            .fillMaxSize()
                    )
                    {
                        Row(
                        ) {

                            Box(modifier.size(125.dp)) {
                                AsyncImage(
                                    model = users[index].image, // Use user.image URL directly
                                    contentDescription = "Profile picture of ${users[index].businessName}",
                                    modifier = modifier
                                        .fillMaxSize()
                                        .clip(CircleShape) // Make image circular
                                )
                            }

                            Box(modifier = modifier.align(Alignment.CenterVertically)) {
                                Text(
                                    text = users[index].businessName,
                                    fontSize = 26.sp,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}