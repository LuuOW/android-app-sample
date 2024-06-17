package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.providers.data.SplashUIModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface SplashUIAPI {

    @Headers("Authorization: Bearer ")
    @GET("/api/ui")
    fun getSplashUI(@Query("id") id: String): Call<Array<SplashUIModel>>


}