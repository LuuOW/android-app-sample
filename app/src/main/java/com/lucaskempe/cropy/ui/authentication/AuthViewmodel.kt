package com.lucaskempe.cropy.ui.authentication

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(
    stateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
) : ViewModel() {

    val code = mutableStateOf("")

    val routeIsReset = mutableStateOf(false)

    var selectedCountry = mutableStateOf(Country("AR", "Argentina", "+54", "🇦🇷"))
    var phoneNumber = mutableStateOf("3815172294")

    var email by mutableStateOf("")
        private set

    val emailHasErrors by derivedStateOf {
        if (email.isNotEmpty()) {
            // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    var password by mutableStateOf("")
        private set

    var repeatPassword by mutableStateOf("")
        private set

    val passwordHasError by derivedStateOf {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        if (password.isNotEmpty()) {
            !password.matches(passwordRegex)
        } else {
            false
        }
    }

    fun updatePassword(input: String) {
        password = input
    }

    fun updateRepeat(input: String) {
        repeatPassword = input
    }

    fun updateEmail(input: String) {
        email = input
    }

    val codeList =  mutableStateOf(List(6) { "" })

    var emailField = mutableStateOf(TextFieldValue(""))

    var usernameField = mutableStateOf(TextFieldValue(""))

    var passwordField = mutableStateOf(TextFieldValue(""))

    var loginPasswordField = mutableStateOf(TextFieldValue(""))

}