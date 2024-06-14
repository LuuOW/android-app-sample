package com.lucaskempe.cropy.ui.authentication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.lucaskempe.cropy.providers.data.SplashUIDataStore
import com.lucaskempe.cropy.providers.data.SplashUIModel
import com.lucaskempe.cropy.services.SplashUIWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@Composable
fun SignUpScreen(
    context: Context,
    activity: Activity,
    navController: NavHostController
) {

    var phoneNumberField by remember { mutableStateOf(TextFieldValue("+543815172294")) }
    var verificationCodeField by remember { mutableStateOf(TextFieldValue("")) }
    var codeSent by remember { mutableStateOf(false) }
    var timeRemaining by rememberSaveable { mutableStateOf(30) }
    var credentialId by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance().apply {
        firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
    }

    var splashData by remember { mutableStateOf(SplashUIModel()) }

    val workManager = WorkManager.getInstance(context)

    LaunchedEffect(key1 = workManager) {
        while (true) {

            workManager.enqueueUniqueWork(
                "one_time_work",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequest.Builder(SplashUIWorker::class.java)
                    .setInputData( workDataOf("id" to "666c705bb45efb24eadc999c"))
                    .build()
            )

            val data = context.SplashUIDataStore.data.first()
            splashData = data
            delay(4000)
        }

    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    navController.navigate("home_graph")
                    auth.createUserWithEmailAndPassword("meme", "meme")
                    verificationCodeField = TextFieldValue("")
                    val user = task.result?.user
                    val profileUpdates = userProfileChangeRequest {
                        displayName = "Jane Q. User"
                        photoUri = Uri.parse("https://m.media-amazon.com/images/M/MV5BOGJjNDBkMzMtMTFmNS00ZGYxLThjZTItZTRkY2FjNmE4ZDAyXkEyXkFqcGdeQXVyMTI1NjU2Mjcz._V1_.jpg")
                    }
                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                            }
                        }
                } else {
                    credentialId = ""
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }
                }
            }
    }


    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeAutoRetrievalTimeOut(p0: String) {

        }

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) { credentialId = verificationId } }


    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = splashData.model_description)
            AsyncImage(
                modifier = Modifier.size(300.dp),
                model = splashData.model_image,
                contentDescription = "")

            val options = PhoneAuthOptions.newBuilder(auth)
                .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks

            Text(text = "Insert phone number")
            TextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = phoneNumberField,
                onValueChange = { phoneNumberField = it })




            if (codeSent == true) {
                Spacer(modifier = Modifier.width(20.dp))

                Text(text = "Insert verification code")

                TextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = verificationCodeField,
                    onValueChange = { verificationCodeField = it })
            }


            Button(
                colors = ButtonColors(
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.Black,
                    containerColor = if (!codeSent) Color(43, 43, 70) else Color.White,
                    contentColor = if (!codeSent) Color.White else Color.Black
                ),
                onClick = {

                    if (verificationCodeField.text.length.equals(6)) {
                        val credential = PhoneAuthProvider.getCredential(credentialId, verificationCodeField.text)
                        signInWithPhoneAuthCredential(credential)
                    }

                    if (isValidPhoneNumber(phoneNumberField.text) && !verificationCodeField.text.length.equals(6)) {
                        codeSent = !codeSent
                        if (codeSent) {
                            timeRemaining = 30
                        }

                        //signInLauncher.launch(signInIntent)
                        options.apply {
                            setPhoneNumber(phoneNumberField.text)
                        }.also {
/*                    auth.apply {
                        firebaseAuthSettings
                            .setAutoRetrievedSmsCodeForPhoneNumber(
                                phoneNumberField.text,
                                verificationCodeField.text
                            )
                    }*/
                            PhoneAuthProvider.verifyPhoneNumber(it.build())
                        }
                    }
                }) {


                fun formatTime(timeRemaining: Int): String {
                    val minutes = timeRemaining / 60
                    val seconds = timeRemaining % 60
                    return if (seconds < 1) {
                        "Resend Code"
                    } else {
                        "Wait ${String.format("%02d:%02d", minutes, seconds)} to resend"
                    }
                }

                // create a timer to resend the code -60
                if (codeSent) {
                    LaunchedEffect(Unit) {
                        while (timeRemaining > 0) {
                            delay(1000)
                            timeRemaining--
                        }
                    }
                }
                Text(
                    text = when {
                        verificationCodeField.text.length.equals(6) && codeSent -> "Verify"
                        codeSent && !verificationCodeField.text.length.equals(6)-> formatTime(timeRemaining)
                        else -> "Send Code"
                    }
                )
            }


        }

    }


}

fun isValidPhoneNumber(phoneNumber: String): Boolean {
    val pattern = Regex("^(?:\\+?54)?0?(?:11|[2368]\\d)(?:\\d{7,8})$")
    return pattern.matches(phoneNumber)
}
