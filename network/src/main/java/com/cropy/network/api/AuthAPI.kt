package com.cropy.network.api

import com.cropy.entities.network.request.CodeVerifyRequest
import com.cropy.entities.network.request.EmailRequest
import com.cropy.entities.network.request.LoginRequest
import com.cropy.entities.network.request.PasswordResetRequest
import com.cropy.entities.network.request.SMSRequest
import com.cropy.entities.network.request.SMSVerifyRequest
import com.cropy.entities.network.request.SignUpRequest
import com.cropy.entities.network.response.CodeResponse
import com.cropy.entities.network.response.LoginResponse
import com.cropy.entities.network.response.MailVerificationResponse
import com.cropy.entities.network.response.SMSResponse
import com.cropy.entities.network.response.SMSVerifyResponse
import com.cropy.entities.network.response.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {

    @POST("/api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/api/email/request")
    fun requestEmail(@Body request: EmailRequest): Call<MailVerificationResponse>

    @POST("/api/email/verify")
    fun verifyCode(@Body request: CodeVerifyRequest): Call<CodeResponse>

    @POST("/api/sms/verify")
    fun verifySMSCode(@Body request: SMSVerifyRequest): Call<SMSVerifyResponse>

    @POST("/api/sms/request")
    fun requestSMS(@Body request: SMSRequest): Call<SMSResponse>

    @POST("/api/password")
    fun resetPassword(@Body request: PasswordResetRequest): Call<String>

    @POST("/api/signup")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

}