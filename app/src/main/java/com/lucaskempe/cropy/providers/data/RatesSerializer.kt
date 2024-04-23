package com.lucaskempe.cropy.providers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.lucaskempe.cropy.services.models.ExchangeRateResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object RatesSerializer: Serializer<ExchangeRateResponse> {

    private val gson = Gson()

    override val defaultValue: ExchangeRateResponse
        get() = ExchangeRateResponse(
            provider = "",
            warning = "",
            terms = "",
            base = "",
            date = "",
            timeLastUpdated = 0,
            rates = mapOf("" to 0.0)
        )

    override suspend fun readFrom(input: InputStream): ExchangeRateResponse {
        return gson.fromJson(input.reader(), ExchangeRateResponse::class.java)
    }

    override suspend fun writeTo(t: ExchangeRateResponse, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(gson.toJson(t).toByteArray())
        }
    }

}

val Context.ratesDataStore: DataStore<ExchangeRateResponse> by dataStore(
    fileName = "rates.pb",
    serializer = RatesSerializer
)