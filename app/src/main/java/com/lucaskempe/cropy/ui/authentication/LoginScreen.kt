package com.lucaskempe.cropy.ui.authentication

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.lucaskempe.cropy.providers.data.SplashUIModel
import com.lucaskempe.cropy.providers.data.UserSessionDataStore
import com.lucaskempe.cropy.services.workers.LoginWorker
import com.lucaskempe.cropy.ui.cards.ValidatingPasswordTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.log


@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: AuthViewmodel
) {

    val workManager: WorkManager = WorkManager.getInstance(vm.context)

    var splashData by remember { mutableStateOf(SplashUIModel()) }
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    val vectorPainter = rememberVectorPainter(
        defaultWidth = 530.46f.dp,
        defaultHeight = 563.1f.dp,
        viewportWidth = 530.46f,
        viewportHeight = 563.1f,
        autoMirror = true,
    ) { viewPortWidth, viewPortHeight ->

    }

    Image(
        vectorPainter,
        contentDescription = "Jellyfish",
        modifier = Modifier
            .fillMaxSize()
            .background(largeRadialGradient)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = splashData.model_title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Monospace,
            textDecoration = TextDecoration.Underline
        )

        Spacer(Modifier.padding(vertical = 10.dp))

        Text(
            text = splashData.model_description,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Monospace,
            textDecoration = TextDecoration.Underline
        )

        Spacer(Modifier.padding(vertical = 10.dp))

        OutlinedTextField(
            value = vm.usernameField.value,
            onValueChange = { vm.usernameField.value = it },
            label = { Text("Username: ", color = Color.White) },
            //visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        ValidatingPasswordTextField(
            inputValue = vm.password,
            updateState = { input -> vm.updatePassword(input) },
            validatorHasErrors = false,
            isRepeat = false
        )

        Spacer(Modifier.padding(vertical = 10.dp))

        Row {
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    disabledElevation = 0.dp
                ),
                modifier = Modifier,
                enabled = !loading,
                onClick = {
                    loading = true
                    login(workManager, navController, vm)
                }) {
                Text(
                    text = splashData.model_buttons_titles.first(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Monospace,
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        val skip = false

        ForgotPasswordLabel {
            if (skip != true) {
                vm.routeIsReset.value = true
                navController.navigate("sms_request")
            } else {
                vm.routeIsReset.value = true
                navController.navigate("reset")
            }
        }

        Spacer(Modifier.padding(vertical = 10.dp))

        if (!loading) return

        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

    }
}

@Composable
fun IndeterminateCircularIndicator() {
    var loading by remember { mutableStateOf(false) }

    Button(onClick = { loading = true }, enabled = !loading) {
        Text("Start loading")
    }

    if (!loading) return

    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun ForgotPasswordLabel(onClick: () -> Unit) {
    Text(
        text = "Forgot Password?",
        style = MaterialTheme.typography.bodySmall, // Adjust style as needed
        modifier = Modifier
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.primary // Or use a custom color
    )
}

fun login(
    workManager: WorkManager,
    navController: NavHostController,
    vm: AuthViewmodel
) {
    val workRequest = OneTimeWorkRequest.Builder(LoginWorker::class.java)
        .setInputData(
            workDataOf(
                "username" to vm.usernameField.value.text,
                "password" to vm.loginPasswordField.value.text
            )
        )
        .build()
    workManager.enqueueUniqueWork("one_time", ExistingWorkPolicy.KEEP, workRequest)

    CoroutineScope(Dispatchers.Main).launch {
        delay(3000)
        vm.context.UserSessionDataStore.data.first().token.let {
            if (it != "") {
                navController.navigate("home_graph") {
                    launchSingleTop = true
                    restoreState = false
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                        saveState = false
                    }
                }
            }
        }
    }


}


@Preview
@Composable
fun LoginPreview() {
    val vm: AuthViewmodel = hiltViewModel()
    val navController = rememberNavController()
    LoginScreen(navController, vm = vm)

}