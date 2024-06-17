package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.request.EmailRequest
import com.lucaskempe.cropy.services.workers.MailRequestWorker
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface MailRequestAPI {

    @POST("/api/email/send")
    fun requestEmail(@Body request: EmailRequest): Call<MailRequestWorker.VerificationResponse>

}