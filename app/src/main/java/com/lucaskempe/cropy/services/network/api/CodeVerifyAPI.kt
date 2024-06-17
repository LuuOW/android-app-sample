package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.request.CodeVerifyRequest
import com.lucaskempe.cropy.services.models.request.LoginRequest
import com.lucaskempe.cropy.services.models.response.LoginResponse
import com.lucaskempe.cropy.services.workers.CodeVerifyWorker
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CodeVerifyAPI {

    companion object {
        const val apiKey: String = BuildConfig.API_KEY

    }
    @Headers("Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2Nsb3VkLmdvb2dsZS5jb20vd29ya3N0YXRpb25zIiwiYXVkIjoiaWR4LWNyb3B5YmFjay0xNzE4NDYwNzczODAyLmNsdXN0ZXItZXRzcXJxdnF5dmQ0ZXJ4eDdxcTMyaW1yamsuY2xvdWR3b3Jrc3RhdGlvbnMuZGV2IiwiaWF0IjoxNzE4NTc0MzkwLCJleHAiOjE3MTg1Nzc5OTB9.XlneH7145DcHtQFFsaWcfvLIepVUMWHXkTOj4RO5ch9306zOCynnztUzNtJ6TUimjS3O-stHtIImumZkEEpKZdKw01Mi_vYpDmzwqLRcPbnlaROY7Wy15he5b46jdD64ggxLGAdhG3tZHHyg96tmNnVAChUQnZZSfXBV8k89g5o7dWNqvLg0aSHVLiakzDhH30kq7QcTjWNhoGKtjfhwmYZQMSYjbCTtNW_aicZbk00IKHi7QdbM0lif-Mddd9SFVY9NthiOyX3lL78AFJK84BMRScw8ZWHGJ-9UGdrmwmDZxHtUcXCkJMVb2ep2N_j4tIPn-prv7mfkdEZc_YHj9Q")
    @POST("/api/email/verify")
    fun verifyCode(@Body request: CodeVerifyRequest): Call<CodeVerifyWorker.CodeResponse>

}