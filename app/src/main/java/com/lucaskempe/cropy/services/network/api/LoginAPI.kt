package com.lucaskempe.cropy.services.network.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<String>
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)