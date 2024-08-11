package com.lucaskempe.cropy.ui.authentication

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.work.WorkManager
import com.cropy.entities.network.request.EmailRequest
import com.cropy.entities.network.request.LoginRequest
import com.cropy.entities.network.request.PasswordResetRequest
import com.cropy.entities.network.request.SMSRequest
import com.cropy.entities.network.request.SMSVerifyRequest
import com.cropy.entities.network.request.SignUpRequest
import com.cropy.repositories.api.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
    val workManager: WorkManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    var code by stateHandle.saveable { mutableStateOf("") }

    var routeIsReset by stateHandle.saveable{ mutableStateOf(false) }

    var selectedCountry by stateHandle.saveable{ mutableStateOf(Country("AR", "Argentina", "+54", "🇦🇷")) }
    var phoneNumber by stateHandle.saveable { mutableStateOf("3815172294") }

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

    var usernameField by  mutableStateOf(TextFieldValue(""))

    suspend fun login(): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val loginReq = LoginRequest(usernameField.text, password)
        val loginCall = authRepository.login(loginReq)
        val loginResult = loginCall.getOrNull()
        if (loginResult != null && loginCall.isSuccess) {
            state.emit(true)
        } else {
            state.emit(false)
        }
        return state
    }

    suspend fun signUp(): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val request = SignUpRequest(email, selectedCountry.dialCode.plus(phoneNumber), usernameField.text, repeatPassword)
        val call = authRepository.signUp(request)
        val result = call.getOrNull()
        if (call.isSuccess && result != null) {
            state.emit(true)
        } else {
            state.emit(false)
        }
        return state
    }

    suspend fun verifySMSCode(): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val smsReq = SMSVerifyRequest(selectedCountry.dialCode.plus(phoneNumber),code)
            val result = authRepository.verifySMSCode(smsReq)
            val resultData = result.getOrNull()
            if(result.isSuccess && resultData != null) {
                if (resultData) {
                    state.emit(true)
                } else {
                    state.emit(false)
                }
            } else {
                state.emit(false)
            }
        return state
    }

    suspend fun requestSMSCode(): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val request = SMSRequest(selectedCountry.dialCode.plus(phoneNumber))
        val call = authRepository.requestSMSCode(request)
        val result = call.getOrNull()
        if (call.isSuccess && result !=null) {
            state.emit(true)
        } else {
            state.emit(false)
        }
        return state
    }

    suspend fun requestEmailCode(): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val request = EmailRequest(email)
        val call = authRepository.requestEmailCode(request)
        val result = call.getOrNull()
        if (call.isSuccess && result != null) {
            state.emit(true)
        } else {
            state.emit(false)
        }
        return state
    }

    suspend fun resetPassword() {
        val request = PasswordResetRequest(selectedCountry.dialCode.plus(phoneNumber), password)
        authRepository.resetPassword(request)
    }


}