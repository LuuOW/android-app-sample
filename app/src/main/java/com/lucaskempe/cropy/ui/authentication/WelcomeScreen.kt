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
import androidx.credentials.CredentialManager
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
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialCustomException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.credentials.exceptions.CreateCredentialProviderConfigurationException
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.util.Base64
import java.util.concurrent.TimeUnit


@Composable
fun WelcomeScreen(navController: NavHostController) {

    var splashData by remember { mutableStateOf(UIModel()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    fun createPasskey(requestJson: String, preferImmediatelyAvailableCredentials: Boolean) {
        val createPublicKeyCredentialRequest = CreatePublicKeyCredentialRequest(
            // Contains the request in JSON format. Uses the standard WebAuthn
            // web JSON spec.
            requestJson = requestJson,
            // Defines whether you prefer to use only immediately available
            // credentials, not hybrid credentials, to fulfill this request.
            // This value is false by default.
            preferImmediatelyAvailableCredentials = preferImmediatelyAvailableCredentials,
        )

        // Execute CreateCredentialRequest asynchronously to register credentials
        // for a user account. Handle success and failure cases with the result and
        // exceptions, respectively.
        coroutineScope.launch {
            try {
                val result = credentialManager.createCredential(
                    // Use an activity-based context to avoid undefined system
                    // UI launching behavior
                    context = context,
                    request = createPublicKeyCredentialRequest,
                )
                //handlePasskeyRegistrationResult(result)
            } catch (e : CreateCredentialException){
                handleFailure(e)
            }
        }
    }


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
                    createPasskey(
                        requestJson = """
                            {
                              "challenge": "T1xCsnxM2DNL2KdK5CLa6fMhD7OBqho6syzInk_n-Uo",
                              "rp": {
                                "name": "Cropy",
                                "id": "https://localhost:8020"
                              },
                              "user": {
                                "id": "T1sCsnxM2DNL2KdK5CLa6fMhD7OBqho6syzInk_n-Uo",
                                "name": "lucas.kempe@icloud.com",
                                "displayName": "Lucas Kempe"
                              },
                              "pubKeyCredParams": [
                                {
                                  "type": "public-key",
                                  "alg": -7
                                },
                                {
                                  "type": "public-key",
                                  "alg": -257
                                }
                              ],
                              "timeout": 1800000,
                              "attestation": "none",
                              "excludeCredentials": [
                                {"id": "ghi789", "type": "public-key"},
                                {"id": "jkl012", "type": "public-key"}
                              ],
                              "authenticatorSelection": {
                                "authenticatorAttachment": "platform",
                                "requireResidentKey": true,
                                "residentKey": "required",
                                "userVerification": "required"
                              }
                            }
                        """.trimIndent(),
                        preferImmediatelyAvailableCredentials = true
                    )
                    //navController.navigate("sms_request")
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

fun generatePasskeyChallenge(): String {
    val random = SecureRandom()
    val challenge = ByteArray(16) // 16 bytes = 128 bits
    random.nextBytes(challenge)

    // Encode to Base64 (recommended for web/string representation)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(challenge)
}


fun handleFailure(e: CreateCredentialException) {
    when (e) {
        is CreatePublicKeyCredentialDomException -> {
            // Handle the passkey DOM errors thrown according to the
            // WebAuthn spec.

            //handlePasskeyError(e.domError)
        }
        is CreateCredentialCancellationException -> {
            // The user intentionally canceled the operation and chose not
            // to register the credential.
        }
        is CreateCredentialInterruptedException -> {
            // Retry-able error. Consider retrying the call.
        }
        is CreateCredentialProviderConfigurationException -> {
            // Your app is missing the provider configuration dependency.
            // Most likely, you're missing the
            // "credentials-play-services-auth" module.
        }
        is CreateCredentialUnknownException -> {

        }
        is CreateCredentialCustomException -> {
            // You have encountered an error from a 3rd-party SDK. If you
            // make the API call with a request object that's a subclass of
            // CreateCustomCredentialRequest using a 3rd-party SDK, then you
            // should check for any custom exception type constants within
            // that SDK to match with e.type. Otherwise, drop or log the
            // exception.
        }
        else -> Log.w(TAG, "Unexpected exception type ${e::class.java.name}")
    }
}

@Preview
@Composable
fun WelcomePreview() {
    val navController = rememberNavController()
    WelcomeScreen(navController = navController)
}