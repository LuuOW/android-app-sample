package com.lucaskempe.cropy.ui.authentication

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.cropy.datastore.ui.UIDataStore
import com.cropy.entities.ui.UIModel
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.face
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.freckle1
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.freckle2
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.freckle3
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.freckle4
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.leftEye
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.mouth
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.outerJelly
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.rightEye
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath1
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath2
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath3
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath4
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath5
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath6
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath7
import com.lucaskempe.cropy.ui.authentication.JellyFishPaths.tentaclePath8
import com.lucaskempe.cropy.ui.cards.ValidatingInputTextField
import com.lucaskempe.cropy.ui.cards.ValidatingPasswordTextField
import com.lucaskempe.cropy.ui.emailHasErrors
import com.lucaskempe.cropy.ui.passwordHasError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(
    signUp: (email: String, password: String, username: String) -> Unit = { email, password, username -> }
) {

    var splashData by remember { mutableStateOf(UIModel()) }
    val context = LocalContext.current

    var repeatPassword by remember { mutableStateOf( TextFieldValue("") ) }

    var password by remember { mutableStateOf( TextFieldValue("") ) }
    var email by remember { mutableStateOf( TextFieldValue("") ) }
    var usernameField by remember { mutableStateOf( TextFieldValue("") ) }

    LaunchedEffect(Unit) {
        context.UIDataStore.data.collect { it.getOrNull(2)?.let { splashData = it } }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
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

        ValidatingInputTextField(
            colors = OutlinedTextFieldDefaults.colors().copy(focusedTextColor = Color.White),
            inputValue = email,
            updateState = { input -> email = input },
            validatorHasErrors = emailHasErrors(email),
            field = "Email"
        )

        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors().copy(focusedTextColor = Color.White),
            value = usernameField,
            onValueChange = { usernameField = it },
            label = { Text("Username: ", color = Color.White) },
            //visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )


        ValidatingPasswordTextField(
            colors = OutlinedTextFieldDefaults.colors().copy(focusedTextColor = Color.White),
            inputValue = password,
            updateState = { input -> password = input },
            validatorHasErrors = passwordHasError(password),
            isRepeat = false,
        )

        ValidatingPasswordTextField(
            colors = OutlinedTextFieldDefaults.colors().copy(focusedTextColor = Color.White),
            inputValue = repeatPassword,
            updateState = { input -> repeatPassword = input },
            validatorHasErrors = password.text != repeatPassword.text,
            isRepeat = true
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
                onClick = {
                    signUp(email.text, password.text, usernameField.text)
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

@Preview
@Composable
fun SignUpPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    SignUpScreen()
}

object JellyFishPaths {
    val tentaclePath =
        PathParser().parsePathString("M226.31 258.64c.77 8.68 2.71 16.48 1.55 25.15-.78 8.24-5 15.18-7.37 23-3.1 10.84-4.65 22.55 1.17 32.52 4.65 7.37 7.75 11.71 5.81 21.25-2.33 8.67-7.37 16.91-2.71 26 4.26 8.68 7.75 4.34 8.14-3 .39-12.14 0-24.28.77-36 .78-16.91-12-27.75-2.71-44.23 7-12.15 11.24-33 7.76-46.83z")
            .toNodes()
    val tentaclePath1 =
        PathParser().parsePathString("M246.47 259.51a75 75 0 0 1 3.1 33.39c-1.55 11.71-8.91 20.82-8.91 33 0 10 3.48 18.21 5.81 27.75 2.71 10.41.39 19.51-.78 30.35-1.55 9.11-4.65 24.29-.77 33.39s7.37 4.77 7.37-2.6c0-6.07-.78-11.71-1.17-17.77-.39-8.25 3.1-12.58 5-20.39 5.43-21.24-9.3-42.06-3.49-63.31 2.72-11.27 10.86-22.55 11.25-34.26.38-6.07-1.55-14.74-2.72-20.38z")
            .toNodes()
    val tentaclePath2 =
        PathParser().parsePathString("M276.54 262.11c-1.55 9.11 2.21 17.35-.51 26.89-3.87 12.57-8.2 23.41-8.58 37.29 0 10.41 5.78 15.61 7.72 25.15 2.72 11.28-2.73 19.95-5.44 30.35-1.94 6.51-3.11 15.62-.79 22.12 3.49 8.24 5.43 7.81 3.88 18.21-.78 4.77-2.72 17.35 4.26 19.09 5.43.86 5.82-6.94 5.82-11.72-.39-12.57-3.49-24.28-3.11-37.29.39-14.31 4.66-27.75 5.82-42.06.39-7.37-1.94-13-3.49-19.95-2.33-10.4-.78-15.17 3.1-24.71 4.65-10.85 5.28-21.25 6.06-33 .77-6.07 2.53-13-.54-18.65v.44z")
            .toNodes()
    val tentaclePath3 =
        PathParser().parsePathString("M300.34 261.25c1.55 14.3 7 19.94 1.17 34.68-5.43 12.15-8.53 22.12-3.88 35.13 3.88 10.84 5.43 18.21 3.88 30.36-1.56 10-2.33 19.94 4.26 28.61 13.57 17.35 10.86-10.4 8.14-19.07-1.55-6.08-3.49-11.71-2.71-18.22 1.16-8.24 3.88-14.31 1.55-23-1.55-6.07-5-10.41-3.88-17.35s5.82-12.14 6.59-19.51c1.17-7.37 3.49-30.35-3.1-35.56z")
            .toNodes()
    val tentaclePath4 =
        PathParser().parsePathString("M254.44 253.48c-8.78 25.12-15.81 53.28 2.61 77.35 11.06 14.46 42.58 51.91 38.16 69.8-3.91 15.83-20.78 24.73-14.17 28.18 8.22 3.21 22.2-26 18.7-41.25-3.89-16.94-17.68-35.47-34.38-54.78-2.48-2.86-13.15-18.09-14-35.58-1-19.92 12.84-42.61 12.38-42.62-3.13-.02-8.1-4.53-9.3-1.1z")
            .toNodes()
    val tentaclePath5 =
        PathParser().parsePathString("M276.84 253.3c7.24 20.7 13 43.91-2.15 63.75-9.11 11.91-35.1 42.78-31.45 57.53 3.22 13.05 13.8 19.9 11.68 23.22-2.87 3.86-16.15-16.76-16.55-25.86-.72-16.2 12.73-33.92 29.47-53.28 2-2.36 10.84-14.91 11.57-29.33.82-16.41-10.59-35.12-10.2-35.12 2.54-.02 6.64-3.74 7.63-.91z")
            .toNodes()
    val tentaclePath6 =
        PathParser().parsePathString("M295.27 254.27c5.83 16.69 12.69 18.82 1.58 35.22a45 45 0 0 0-6.67 19.22c-.52 6.52 6.09 13.33 5.5 19.6-2.35 24.92-17.13 46.55-19.33 44.29-4.35-4.6 22.07-28.6 12.46-51.16-8.38-19.64 6.49-36.53 9.9-42.49 2.75-4.8-11.51-24.51-11.42-24.51 2.05-.01 5.93-.6 7.98-.17z")
            .toNodes()
    val tentaclePath7 =
        PathParser().parsePathString("M256.56 252.22c-7 6.09-17.21 14-23.17 24-5.55 9.25-7.06 20.43-7.66 27.88-.53 6.52 2.21 13.76 5.49 19.6 14 24.92-15.62 46.55-19.33 44.29-4.55-4 14.59-16.95 17.41-23.45 2.91-6.69-.07-16.29-5-27.71-8.38-19.64 1-39.25 4.37-45.2 2.75-4.81 19.82-19 19.91-19 2.1-.07 5.92-.84 7.98-.41z")
            .toNodes()
    val tentaclePath8 =
        PathParser().parsePathString("M288.76 254.4c7 6.09 17.21 14 23.17 24 5.55 9.25 7.06 20.43 7.66 27.88.53 6.52-2.21 13.76-5.49 19.6-14 24.93 9 45 19.33 44.29 6.58-4.07-14.59-16.95-17.41-23.45-2.91-6.69.07-16.28 5-27.71 8.38-19.64-1-39.25-4.37-45.2-2.75-4.81-19.82-19-19.91-19-2.1-.06-5.92-.81-7.98-.41z")
            .toNodes()

    val face =
        PathParser().parsePathString("M283.26 178.86c-17.72-1.84-36.33 5.33-52.24 17.37a110.1 110.1 0 0 0-22.14 22.06c-8.54 11.67-14 31.93-7.48 47.29 8.82 20.73 29.67 14.17 44.32 15.6 14.34 1.55 28.68-.52 43-.52 10.47 0 20.94 1 31.4-.51 9.36-1.39 21.38-9.12 24-22.07 4.24-21-13.77-45.52-24.27-58.37-10.69-13.1-23.39-19.48-36.59-20.85z")
            .toNodes()
    val outerJelly =
        PathParser().parsePathString("M340.52 295.71c-10.12 1.19-15.63-.12-20.38-1.64-3.63-1.17-6.84-2.44-11.37-2.83l-1-.06c-1-.06-2.05-.06-3.17 0a21.37 21.37 0 0 0-8 2.28c-1.27.59-2.6 1.21-4.09 1.82a42.23 42.23 0 0 1-10.6 3c-1 .16-2 .29-3.11.37-1.56.14-3.26.22-5.09.22h-.51c-1.74 0-3.35-.08-4.84-.2-.6 0-1.15-.08-1.7-.15a47.37 47.37 0 0 1-9.43-1.8 8.25 8.25 0 0 1-.92-.29 75 75 0 0 1-8-3.34L248 293a19.06 19.06 0 0 0-6.86-1.81c-1.27-.06-2.44-.12-3.56-.14h-1a51.6 51.6 0 0 0-9.87.67c-8 1.38-10.86 4.29-22.82 4.06-22.13 0-9.51-11.13-11-17.8-7.24-33.11-.69-63.69 18-83.91 14.65-15.86 35.74-24.25 61-24.25 23.56 0 43.94 8.81 59 25.44 19.21 21.29 27 53.84 20 83-1.67 6.46 10.68 14.95-10.37 17.45z")
            .toNodes()

    val freckle1 =
        PathParser().parsePathString("M330 225.23a4.16 4.16 0 0 0-.1.55c-.52 3.79 3.9 6.73 5.91 3 2.42-4.46-4.66-8.14-5.81-3.55z")
            .toNodes()
    val freckle2 =
        PathParser().parsePathString("M322.89 215.5a3.94 3.94 0 0 0-1.32 3.61 3 3 0 1 0 5.91 0c.27-3.81-2.76-4.98-4.59-3.61z")
            .toNodes()
    val freckle3 =
        PathParser().parsePathString("M206.88 241.24c-.88 2-.11 4.56 3.36 4.27 3.87-.33 5.42-5.85.75-6.62a3.79 3.79 0 0 0-4.11 2.35z")
            .toNodes()
    val freckle4 =
        PathParser().parsePathString("M210.11 225.75c.83-.07 1.7.52 1.92 2.17.4 3-2.5 3.09-3.4.6a2 2 0 0 1 1.48-2.77z")
            .toNodes()

    val mouth = PathParser().parsePathString(
        "M286.84 244s-.11-.12-.32-.31a9.91 9.91 0 0 1-.81-.76l-.5-.53c-.16-.2-.31-.41-.48-.61s-.27-.46-.41-.69a6.83 6.83 0 0 1-.29-.75 5.86 5.86 0 0 1-.06-3 7.71 7.71 0 0 1 .29-1.05 3.46 3.46 0 0 1 .13-.39.49.49 0 1 0-.8-.55 3.31 3.31 0 0 0-.33.38 5.42 5.42 0 0 0-.68 1.12 5.32 5.32 0 0 0-.53 2.93 4.34 4.34 0 0 0 .07.52h-.08a9.59 9.59 0 0 1-1.64.53 11.21 11.21 0 0 1-1.15.22 10.31 10.31 0 0 1-1.29.17 12 12 0 0 1-1.41.1c-.48 0-1.07.05-1.48 0l-1.46-.06c-.48 0-.94-.11-1.39-.15s-.88-.17-1.29-.23-.78-.2-1.13-.29-.65-.22-.93-.31a5 5 0 0 1-.67-.28 2.56 2.56 0 0 0 0-.27 5.08 5.08 0 0 0 0-1.1 6 6 0 0 0-1.12-3 2.65 2.65 0 0 0-.32-.39.5.5 0 0 0-.67-.15.49.49 0 0 0-.15.68 3.33 3.33 0 0 1 .12.39 9.13 9.13 0 0 1 .26 1.06 5.81 5.81 0 0 1-.14 3 6.81 6.81 0 0 1-.31.74l-.43.67c-.18.2-.34.41-.49.61l-.52.51a9 9 0 0 1-.83.74c-.21.18-.33.3-.33.3a.49.49 0 0 0 .5.81s.16-.05.44-.17a6.14 6.14 0 0 0 1.15-.56c.22-.15.48-.3.72-.49s.49-.43.73-.68.45-.57.67-.87.24-.49.35-.74a2.77 2.77 0 0 0 .42.27 7 7 0 0 0 1 .53 7.33 7.33 0 0 0 1.23.48 8.56 8.56 0 0 0 1.41.38c.5.08 1 .2 1.54.24l1.6.09c.61 0 1.05 0 1.58-.07a11.79 11.79 0 0 0 1.53-.18 9.58 9.58 0 0 0 1.44-.31 8.32 8.32 0 0 0 1.25-.42 6.92 6.92 0 0 0 1-.47 5.28 5.28 0 0 0 .6-.36c.09.19.18.37.26.55s.42.62.64.89.48.49.71.71.49.35.71.51a7.4 7.4 0 0 0 1.13.59l.44.18a.47.47 0 0 0 .51-.11.48.48 0 0 0 .01-.6z"
    ).toNodes()
    val leftEye =
        PathParser().parsePathString("M262 233.63a3.1 3.1 0 1 0-3 3.19 3.1 3.1 0 0 0 3-3.19z")
            .toNodes()
    val rightEye =
        PathParser().parsePathString("M294 232.72a3.1 3.1 0 1 0-3 3.18 3.1 3.1 0 0 0 3-3.18z")
            .toNodes()

    val duck = PathParser().parsePathString(
        "M461.045,238.203c-2.87-3.683-7.653-4.951-11.901-3.142c-48.624,20.699-108.001-0.101-141.469-11.917\n" +
                "\tc-19.907-7.028-42.363-11.402-66.325-11.991c-16.056-0.395-31.499,0.973-46.054,3.7c-2.132,0.399-4.127-1.178-4.264-3.343\n" +
                "\tc-0.598-9.486,0.263-15.81,0.263-15.81c15.415-108.168-44.707-138.149-78.28-138.149c-24.677,0-44.625,17.054-49.761,39.844H40.776\n" +
                "\tc-5.308,0-10.117,3.514-11.357,8.675c-0.932,3.879,0.322,7.331,2.554,9.889l-28.976,5.144c-1.766,0.308-3.04,1.868-2.996,3.662\n" +
                "\tc0.043,1.787,1.392,3.281,3.17,3.508c51.344,6.599,73.455,13.116,83.415,18.563c32,17.5,35.582,42.939,26.427,55.346\n" +
                "\tc-39.476,53.491-29.867,96.754-20.345,122.863c15.876,43.531,72.153,79.02,143.882,80.788c46.206,1.139,86.9-12.887,115.695-32.471\n" +
                "\tc49.269-33.508,87.886-84.121,109.612-122.005C464.247,247.183,463.924,241.888,461.045,238.203z M346.737,271.587\n" +
                "\tc-12.1,35.647-32.498,60.74-60.637,74.597c-18.999,9.353-38.716,12.202-55.657,12.202c-25.356,0-44.509-6.372-45.813-6.819\n" +
                "\tc-3.918-1.333-6.013-5.588-4.68-9.507s5.588-5.962,9.507-4.695c4.299,1.45,103.887,33.779,141.233-65.449\n" +
                "\tc-12.078-7.859-50.544-26.836-110.625,0.732c-3.75,1.721-8.21,0.081-9.946-3.691c-1.729-3.765-0.073-8.218,3.691-9.946\n" +
                "\tc81.182-37.251,128.745,2.769,130.737,4.497C346.847,265.508,347.711,268.701,346.737,271.587z"
    ).toNodes()
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

@Composable
fun JellyfishAnimation() {
    val vectorPainter = rememberVectorPainter(
        defaultWidth = 530.46f.dp,
        defaultHeight = 563.1f.dp,
        viewportWidth = 530.46f,
        viewportHeight = 563.1f,
        autoMirror = true,
    ) { viewPortWidth, viewPortHeight ->

        val duration = 3000
        val transition = rememberInfiniteTransition()
        val translationY by transition.animateFloat(
            initialValue = 0f,
            targetValue = -30f,
            animationSpec = infiniteRepeatable(
                tween(duration, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            )
        )

        Group(
            translationY = translationY,
            name = "jellyfish"
        ) {
            Group("tentacles") {
                Path(
                    pathData = tentaclePath,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = tentaclePath1,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = tentaclePath2,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = tentaclePath3,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = tentaclePath4,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = tentaclePath5,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = tentaclePath6,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = tentaclePath7,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = tentaclePath8,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = face,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = outerJelly,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
            }

            Group(name = "body") {
/*                Path(
                    pathData = duck,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )*/
            }


            Group(name = "freckles") {
                Path(
                    pathData = freckle1,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = freckle2,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = freckle3,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )
                Path(
                    pathData = freckle4,
                    fill = SolidColor(Color.White),
                    fillAlpha = 0.49f
                )

            }

        }

        Group(name = "face", translationY = translationY) {
            Path(
                pathData = rightEye,
                fill = SolidColor(Color.White),
                fillAlpha = 0.49f
            )
            Path(
                pathData = leftEye,
                fill = SolidColor(Color.White),
                fillAlpha = 0.49f
            )
            Path(
                pathData = mouth,
                fill = SolidColor(Color.White),
                fillAlpha = 0.49f
            )
        }

        Group(name = "bubbles") {
            // bubbles around the jellyfish
        }
    }

    Image(
        vectorPainter,
        contentDescription = "Jellyfish",
        modifier = Modifier
            .fillMaxSize()
            .background(largeRadialGradient)
    )
}