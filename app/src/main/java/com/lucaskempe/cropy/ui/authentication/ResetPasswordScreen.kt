package com.lucaskempe.cropy.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.cropy.entities.ui.UIModel
import com.lucaskempe.cropy.ui.cards.ValidatingPasswordTextField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    navController: NavHostController,
    vm: AuthViewmodel
) {

    var splashData by remember { mutableStateOf(UIModel()) }
    val scope = rememberCoroutineScope()

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
            text = splashData.model_description,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Monospace,
            textDecoration = TextDecoration.Underline
        )

        Spacer(Modifier.padding(vertical = 10.dp))

        ValidatingPasswordTextField(
            inputValue = vm.password,
            updateState = { input -> vm.updatePassword(input) },
            validatorHasErrors = vm.passwordHasError,
            isRepeat = false,
        )

        ValidatingPasswordTextField(
            inputValue = vm.repeatPassword,
            updateState = { input -> vm.updateRepeat(input) },
            validatorHasErrors = vm.password != vm.repeatPassword,
            isRepeat = true
        )


        Row {
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    disabledElevation = 0.dp
                ),
                modifier = Modifier,
                onClick = {
                    scope.launch(Dispatchers.IO){
                        vm.resetPassword()
                    }
                    navController.navigate("login") {
                        launchSingleTop = true
                        restoreState = false
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                            saveState = false
                        }
                    }
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
    }

}