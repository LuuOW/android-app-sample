package com.lucaskempe.cropy.ui.screens

import SelectImageScreen
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InspectorsScreen(
    modifier: Modifier,
    navController: NavController,
    activityResultLauncher: ActivityResultLauncher<Array<String>>
) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val imageDescription = remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult =  {selectedImage=it}
    )

    Column(
    ) {
        Row(
        ) {
            Button(
                onClick = { launcher.launch("image/*") }) {
                Text(text = "Select Image")
            }
            Spacer(modifier = modifier.width(30.dp).padding(16.dp))
            Button(
                onClick = {
                    activityResultLauncher.launch(
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                    navController.navigate("inspector/image_capture")
                }) {
                Text(text = "Launch Camera")
            }
        }
        Text(
            text = "Image Description:" + imageDescription.value,
            modifier = modifier.padding(10.dp)
        )
        selectedImage?.let {
            SelectImageScreen(
                context = LocalContext.current,
                selectedImage = it,
                imageDescription = imageDescription)
        }
    }
}

