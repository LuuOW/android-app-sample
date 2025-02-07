package com.lucaskempe.cropy.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cropy.components.colors.AppColor
import com.cropy.datastore.ui.UIDataStore
import com.cropy.datastore.user.UserSessionDataStore
import com.cropy.entities.ui.UIModel
import com.lucaskempe.cropy.ui.cards.ValidatingPasswordTextField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    login: (username: String, password: String) -> Unit = { user, pass -> },
    forgotPassword: () -> Unit = { }
) {

    var splashData by remember { mutableStateOf(UIModel()) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var password by remember { mutableStateOf(TextFieldValue("")) }
    var usernameField by remember { mutableStateOf( TextFieldValue("") ) }


    LaunchedEffect(Unit) {
        context.UIDataStore.data.collect { it.getOrNull(1)?.let { splashData = it } }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                    login(usernameField.text, password.text)
                   // loading = true
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

        ForgotPasswordLabel({ forgotPassword()}, splashData.model_buttons_titles[1])

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
fun ForgotPasswordLabel(onClick: () -> Unit, title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodySmall, // Adjust style as needed
        modifier = Modifier
            .clickable(onClick = onClick),
        color = AppColor.White
    )
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen()

}