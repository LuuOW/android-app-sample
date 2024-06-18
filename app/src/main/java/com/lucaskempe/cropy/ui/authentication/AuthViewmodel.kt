package com.lucaskempe.cropy.ui.authentication

import android.content.Context
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
    var phoneNumber = mutableStateOf("")

    val codeList =  mutableStateOf(List(6) { "" })

    var emailField = mutableStateOf(TextFieldValue(""))

    var usernameField = mutableStateOf(TextFieldValue(""))

    var phoneNumberField = mutableStateOf(TextFieldValue(""))

    var passwordField = mutableStateOf(TextFieldValue(""))

    var loginPasswordField = mutableStateOf(TextFieldValue(""))

    var repeatedPassword = mutableStateOf(TextFieldValue(""))

}