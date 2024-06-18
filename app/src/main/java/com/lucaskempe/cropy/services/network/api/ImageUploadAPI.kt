package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.services.models.ImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File


interface ImageUploadAPI {

    @Multipart
    @POST("/api/images")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("filename") filename: RequestBody,
        @Part("user_id") user: RequestBody
    ): Call<ImageResponse>
}