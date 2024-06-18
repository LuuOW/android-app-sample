package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.request.CodeVerifyRequest
import com.lucaskempe.cropy.services.models.request.LoginRequest
import com.lucaskempe.cropy.services.models.request.SMSVerifyRequest
import com.lucaskempe.cropy.services.models.response.LoginResponse
import com.lucaskempe.cropy.services.models.response.SMSVerifyResponse
import com.lucaskempe.cropy.services.workers.CodeVerifyWorker
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CodeVerifyAPI {

    @POST("/api/email/verify")
    fun verifyCode(@Body request: CodeVerifyRequest): Call<CodeVerifyWorker.CodeResponse>

    @POST("/api/sms/verify")
    fun verifySMSCode(@Body request: SMSVerifyRequest): Call<SMSVerifyResponse>

}