@file:OptIn(ExperimentalSerializationApi::class)

package com.cropy.datastore.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.cropy.entities.ui.UIModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.protobuf.ProtoBuf

object UIModelSerializer: Serializer<List<UIModel>> {

    override val defaultValue: List<UIModel>
        get() = listOf(UIModel())

    override suspend fun readFrom(input: InputStream): List<UIModel> {
        return ProtoBuf.decodeFromByteArray(input.readBytes())
    }

    override suspend fun writeTo(t: List<UIModel>, output: OutputStream) {
        withContext(Dispatchers.IO + Job()) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
    }

}

val Context.UIDataStore: DataStore<List<UIModel>> by dataStore(
    fileName = "ui_data.pb",
    serializer = UIModelSerializer
)