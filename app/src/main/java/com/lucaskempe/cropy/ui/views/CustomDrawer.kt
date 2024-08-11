package com.lucaskempe.cropy.ui.views


import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.dataStoreFile
import androidx.navigation.NavController
import com.lucaskempe.cropy.ui.navigation.DrawerRoutes
import kotlinx.coroutines.launch

@Composable
fun CustomDrawer(
    modifier: Modifier,
    drawerState: DrawerState,
    context: Context,
    navController: NavController,
    showTopBar: MutableState<Boolean>,
    showBottomBar: MutableState<Boolean>
) {

    val launcher = rememberLauncherForActivityResult(
        onResult = {

        },
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    )

    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS,
    )

    val mItems = listOf(
        DrawerRoutes.Account,
        DrawerRoutes.Settings,
        DrawerRoutes.Info,
        DrawerRoutes.LogOut
    )
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf<ImageVector?>(null) }


    ModalDrawerSheet{
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            mItems.forEach { item ->
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.route
                        )
                    },
                    label = { Text(item.title) },
                    selected = item.icon == selectedItem.value,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            selectedItem.value = null
                        }
                        selectedItem.value = item.icon

                        if (item.route == "logout") {
                            context.dataStoreFile("sessions.pb").delete()
                            showTopBar.value = false
                            showBottomBar.value = false
                            navController.navigate("auth_graph")
                        }

                        if (item.route == "settings") {
                            launcher.launch(permissions)
                        }

                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }
/*
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            LaunchedEffect(Unit) {
                drawerState.open().apply {
                    tween<Unit>(durationMillis = 500)
                }
                if (drawerState.isOpen) {
                    selectedItem.value = null
                }
            }
        },
        gesturesEnabled = false,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    if (selectedItem.value == Icons.Filled.AccountBox) {
                        //val user = auth.currentUser
                        AsyncImage(
                            model = "",
                            contentDescription = "User Photo",
                            modifier = modifier.clip(CircleShape))

                        Text(text = "Welcome Name")
                    }
                    //val user = auth.currentUser
                    AsyncImage(
                        model = "",
                        contentDescription = "User Photo",
                        modifier = modifier
                            .size(200.dp)
                            .clip(CircleShape))

                    Text(text = "Welcome ")
                    Button(onClick = {
                        scope.launch {
                            if (drawerState.isOpen) drawerState.close() else drawerState.open()
                            launcher.launch(
                                arrayOf(
                                    Manifest.permission.POST_NOTIFICATIONS,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            )
                        }
                    }) {
                        Text("Click to open")
                    }
                }
            }

        }
    )*/
}