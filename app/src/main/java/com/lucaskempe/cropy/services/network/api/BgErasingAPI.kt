package com.lucaskempe.cropy.services.network.api

import android.telecom.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface BgErasingAPI {

    @Headers("X-API-Key: INSERT_YOUR_API_KEY_HERE")
    @Multipart
    @POST("v1.0/removebg")
    suspend fun removeBackground(
        @Part imageFile: MultipartBody.Part,
        @Part("size") size: RequestBody
    )
}