package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.services.models.request.SignUpRequest
import com.lucaskempe.cropy.services.models.response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignUpAPI {

    @POST("/api/signup")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

}