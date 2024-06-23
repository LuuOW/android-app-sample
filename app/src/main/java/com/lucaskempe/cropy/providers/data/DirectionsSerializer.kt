package com.lucaskempe.cropy.providers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.lucaskempe.cropy.services.maps.models.Directions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object DirectionsSerializer: Serializer<Directions> {

    private val gson = Gson()

    override val defaultValue: Directions
        get() = Directions()

    override suspend fun readFrom(input: InputStream): Directions {
        return gson.fromJson(input.reader(), Directions::class.java)
    }

    override suspend fun writeTo(t: Directions, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(gson.toJson(t).toByteArray())
        }
    }
}

val Context.DirectionsDataStore: DataStore<Directions> by dataStore(
    fileName = "directions.pb",
    serializer = DirectionsSerializer
)