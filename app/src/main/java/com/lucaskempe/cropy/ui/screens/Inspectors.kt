package com.lucaskempe.cropy.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lucaskempe.cropy.R
import com.lucaskempe.cropy.ui.cards.ButtonSlider
import com.lucaskempe.cropy.viewmodels.UsersViewModel

@Composable
fun InspectorsScreen(
    //modifier: Modifier,
    navController: NavController,
    vm: UsersViewModel,
    context: Context,
    onClick: () -> Unit = { }
) {
    val modifier = Modifier

    //var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            vm.selectedImageUri = it.toString()
        }
    )

    Column(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Button(onClick = { navController.navigate("image_capture") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Camera"
                    )
                    Spacer(modifier = modifier.padding(4.dp))
                    Text(text = "Camera", style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = modifier.padding(24.dp))
                Button(onClick = { launcher.launch("image/*") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_gallery),
                        contentDescription = "Select Image"
                    )
                    Spacer(modifier = modifier.padding(4.dp))
                    Text(text = "Select Image", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = modifier.padding(24.dp))


        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .padding(16.dp)
            ) {
                if (vm.selectedImageUri.isNotEmpty()) {
                    Image(
                        modifier = modifier.fillMaxSize(),
                        bitmap = BitmapFactory
                            .decodeStream(context.contentResolver.openInputStream(Uri.parse(vm.selectedImageUri)))
                            .asImageBitmap(),
                        contentDescription = "Compressed Image",
                    )
                }
            }
        }


    }

}


@Preview
@Composable
fun InspectorsScreenPreview() {
    val vm = UsersViewModel(SavedStateHandle())

    InspectorsScreen(
        navController = rememberNavController(),
        //modifier = Modifier,
        vm = vm,
        context = LocalContext.current
    )
}
