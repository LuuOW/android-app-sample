package com.cropy.network.api

import com.cropy.entities.network.response.ImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImagesAPI {

    @Multipart
    @POST("/api/images")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("filename") filename: RequestBody,
        @Part("user_id") user: RequestBody
    ): Response<ImageResponse>

}