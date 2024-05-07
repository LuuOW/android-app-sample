package com.lucaskempe.cropy.services.network

import com.lucaskempe.cropy.services.models.ImageResponse
import retrofit2.Response
import retrofit2.http.POST


interface ImageUploadAPI {
    @POST("gs://cropy-d163c.appspot.com")
    suspend fun uploadImage(imageUpload: ByteArray): Response<ImageResponse>
}