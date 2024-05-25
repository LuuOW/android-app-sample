package com.lucaskempe.cropy.services.network.api

import com.lucaskempe.cropy.services.models.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateAPI {
    @GET("/v4/latest/{base}")
    suspend fun getExchangeRate(@Path("base") baseCurrency: String): Response<ExchangeRateResponse>
}