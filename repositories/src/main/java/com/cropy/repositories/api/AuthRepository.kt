package com.cropy.repositories.api

import com.cropy.entities.network.request.EmailRequest
import com.cropy.entities.network.request.LoginRequest
import com.cropy.entities.network.request.PasswordResetRequest
import com.cropy.entities.network.request.SMSRequest
import com.cropy.entities.network.request.SMSVerifyRequest
import com.cropy.entities.network.request.SignUpRequest

interface AuthRepository {

    suspend fun login(loginRequest: LoginRequest): Result<String>

    suspend fun signUp(request: SignUpRequest): Result<String>

    suspend fun requestSMSCode(smsRequest: SMSRequest): Result<String>

    suspend fun verifySMSCode(smsVerifyRequest: SMSVerifyRequest): Result<Boolean>

    suspend fun requestEmailCode(emailRequest: EmailRequest): Result<String>

    suspend fun resetPassword(request: PasswordResetRequest): Result<Boolean>

}