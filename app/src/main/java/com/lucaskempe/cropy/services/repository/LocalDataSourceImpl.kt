package com.lucaskempe.cropy.services.repository

import android.content.Context
import com.lucaskempe.cropy.providers.data.ratesDataStore
import com.lucaskempe.cropy.services.models.ExchangeRateResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LocalDataSourceImpl(private val context: Context): LocalDataSource {


    private val job = Job()
    private val coroutineContext = job + Dispatchers.IO

    override fun saveLocalData(data: ExchangeRateResponse) {
        CoroutineScope(coroutineContext).launch {
            context.ratesDataStore.updateData { data }
        }
    }

    override suspend fun getLocalData(): ExchangeRateResponse {
        return context.ratesDataStore.data.first()
    }

}