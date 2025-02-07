package com.lucaskempe.cropy.ui.authentication

import android.graphics.Shader
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.cropy.datastore.ui.UIDataStore
import com.cropy.entities.ui.UIModel
import com.lucaskempe.cropy.R
import com.cropy.workers.UIWorker
import java.util.concurrent.TimeUnit


@Composable
fun WelcomeScreen(navController: NavHostController) {

    var splashData by remember { mutableStateOf(UIModel()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.UIDataStore.data.collect { it.getOrNull(0)?.let { splashData = it } }
    }

    Box(
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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

            Spacer(Modifier.padding(vertical = 5.dp))

            AsyncImage(
                modifier = Modifier.size(600.dp),
                placeholder = painterResource(id = R.drawable.ic_broken_image),
                model = splashData.model_image,
                contentDescription = ""
            )

            Spacer(Modifier.padding(vertical = 5.dp))

            Text(
                text = splashData.model_description,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Monospace,
                textDecoration = TextDecoration.Underline
            )

            Spacer(Modifier.padding(vertical = 5.dp))

            Row {
                Button(
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp,
                        disabledElevation = 0.dp
                    ),
                    modifier = Modifier,
                    onClick = {
                        navController.navigate("login")
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
                Spacer(Modifier.padding(horizontal = 10.dp))
                Button(onClick = {
                    navController.navigate("sms_request")
                }) {
                    Text(
                        text = splashData.model_buttons_titles[1],
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
}

@Preview
@Composable
fun WelcomePreview() {
    val navController = rememberNavController()
    WelcomeScreen(navController = navController)
}