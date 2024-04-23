package com.lucaskempe.cropy.services.repository

import com.lucaskempe.cropy.services.models.ExchangeRateResponse

interface LocalDataSource {

    fun saveLocalData(data: ExchangeRateResponse)

    suspend fun getLocalData(): ExchangeRateResponse

}