package com.lucaskempe.cropy.ui.views


import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lucaskempe.cropy.ui.navigation.DrawerRoutes
import kotlinx.coroutines.launch

@Composable
fun CustomDrawer(
    activityResultLauncher: ActivityResultLauncher<Array<String>>,
    modifier: Modifier,
    drawerState: DrawerState
) {

    val scope = rememberCoroutineScope()
// icons to mimic drawer destinations
    val items = listOf(
        Icons.Filled.Settings,
        Icons.Filled.Info
    )

    val mItems = listOf(
        DrawerRoutes.Info,
        DrawerRoutes.Settings
    )

    val selectedItem = remember { mutableStateOf<ImageVector?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
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
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
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
                Button(onClick = {
                    scope.launch {
                        if (drawerState.isOpen) drawerState.close() else drawerState.open()
                        activityResultLauncher.launch(
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
    )
}