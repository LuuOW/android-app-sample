@file:OptIn(ExperimentalSerializationApi::class)

package com.cropy.datastore.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.cropy.entities.user.UserSession
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

object SessionSerializer : Serializer<UserSession> {

    override val defaultValue: UserSession
        get() = UserSession()

    override suspend fun readFrom(input: InputStream): UserSession {
        return ProtoBuf.decodeFromByteArray(input.readBytes())
    }

    override suspend fun writeTo(t: UserSession, output: OutputStream) {
        withContext(Dispatchers.IO + Job()) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
    }

}

val Context.UserSessionDataStore: DataStore<UserSession> by dataStore(
    fileName = "sessions.pb",
    serializer = SessionSerializer
)