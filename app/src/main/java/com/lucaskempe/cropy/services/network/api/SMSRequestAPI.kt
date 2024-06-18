package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.services.models.request.SMSRequest
import com.lucaskempe.cropy.services.models.response.SMSResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SMSRequestAPI {

    @POST("/api/sms/request")
    fun requestSMS(@Body request: SMSRequest): Call<SMSResponse>

}
