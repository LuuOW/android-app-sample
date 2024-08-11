package com.cropy.network.api

import com.cropy.entities.ui.UIModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerDrivenUIAPI {

    @GET("/api/ui")
    suspend fun getUIById(@Query("id") id: String): Response<UIModel>

    @GET("/api/ui/all")
    suspend fun getAllUI(): Response<List<UIModel>>

}