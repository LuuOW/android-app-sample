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

    suspend fun login(user: String, password: String): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val loginReq = LoginRequest(user, password)
        val loginCall = authRepository.login(loginReq)
        val loginResult = loginCall.getOrNull()
        if (loginResult != null && loginCall.isSuccess) {
            state.emit(true)
        } else {
            state.emit(false)
        }
        return state
    }

    suspend fun signUp(email: String, phone: String, user: String, password: String): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val request = SignUpRequest(email = email, phone =  phone, username = user, password = password)
        val call = authRepository.signUp(request)
        val result = call.getOrNull()
        if (call.isSuccess && result != null) {
            state.emit(true)
        } else {
            state.emit(false)
        }
        return state
    }

    suspend fun verifySMSCode(phone: String, code: String): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val smsReq = SMSVerifyRequest(phone,code)
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

    suspend fun requestSMSCode(phone: String): StateFlow<Boolean> {
        val state = MutableStateFlow(false)
        val request = SMSRequest(phone)
        val call = authRepository.requestSMSCode(request)
        val result = call.getOrNull()
        if (call.isSuccess && result !=null) {
            state.emit(true)
        } else {
            state.emit(false)
        }
        return state
    }

    suspend fun requestEmailCode(email: String): StateFlow<Boolean> {
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

    suspend fun resetPassword(phone: String, password: String) {
        val request = PasswordResetRequest(phone, password)
        authRepository.resetPassword(request)
    }


}