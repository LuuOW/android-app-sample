package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.request.LoginRequest
import com.lucaskempe.cropy.services.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginAPI {
    companion object {
        const val apiKey: String = BuildConfig.API_KEY

    }
    @Headers("Authorization: Bearer $apiKey")
    @POST("/api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}