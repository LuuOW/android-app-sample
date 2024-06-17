package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.services.models.request.PasswordResetRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PasswordResetAPI {

    @POST("/api/password")
    fun resetPassword(@Body request: PasswordResetRequest): Call<String>

}