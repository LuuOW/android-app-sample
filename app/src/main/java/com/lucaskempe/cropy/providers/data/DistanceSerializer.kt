package com.lucaskempe.cropy.providers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.lucaskempe.cropy.services.maps.models.Directions
import com.lucaskempe.cropy.services.maps.models.distance.DistanceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object DistanceSerializer: Serializer<DistanceModel> {

    private val gson = Gson()

    override val defaultValue: DistanceModel
        get() = DistanceModel()

    override suspend fun readFrom(input: InputStream): DistanceModel {
        return gson.fromJson(input.reader(), DistanceModel::class.java)
    }

    override suspend fun writeTo(t: DistanceModel, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(gson.toJson(t).toByteArray())
        }
    }

}

val Context.DistanceDataStore: DataStore<DistanceModel> by dataStore(
    fileName = "distance.pb",
    serializer = DistanceSerializer
)