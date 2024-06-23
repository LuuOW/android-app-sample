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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.lucaskempe.cropy.R
import com.lucaskempe.cropy.providers.data.SplashUIDataStore
import com.lucaskempe.cropy.providers.data.SplashUIModel
import com.lucaskempe.cropy.services.workers.SplashUIWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first


@Composable
fun WelcomeScreen(navController: NavHostController) {

    var splashData by remember { mutableStateOf(SplashUIModel()) }
    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)


    LaunchedEffect(key1 = workManager) {
        while (true) {
            delay(4000)
            workManager.enqueueUniqueWork(
                "one_time_work",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequest.Builder(SplashUIWorker::class.java)
                    .setInputData(workDataOf("id" to "1"))
                    .build()
            )

            val data = context.SplashUIDataStore.data.first()
            splashData = data
        }
    }

    val largeRadialGradient = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val biggerDimension = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(Color(0xFF9FA8DA), Color(0xFF000000)),
                center = size.center,
                radius = biggerDimension / 2f,
                colorStops = listOf(0f, 0.95f)
            )
        }
    }

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
                val skip = false
                Button(onClick = {
                    if (skip == true) {
                        navController.navigate("sign_up")
                    } else {
                        navController.navigate("sms_request")
                    }
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
    WelcomeScreen(navController)
}