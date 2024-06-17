package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.request.EmailRequest
import com.lucaskempe.cropy.services.workers.MailRequestWorker
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MailRequestAPI {

    companion object {
        const val apiKey: String = BuildConfig.API_KEY
    }


    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ5b3VyLWFwcCIsInN1YiI6ImxrZW1kYXNwZSIsImV4cCI6MTcxODU3NTk0NywiaWF0IjoxNzE4NTcyMzQ3LCJyb2xlcyI6WyJVU0VSIl0sInVzZXJuYW1lIjoibGtlbWRhc3BlIn0.xM5WiLwHTQ9CRpI7-JG5OpWY-l17V_BzUHxkr5peo90")
    @POST("/api/email/send")
    fun requestEmail(@Body request: EmailRequest): Call<MailRequestWorker.VerificationResponse>

}