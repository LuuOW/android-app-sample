package com.lucaskempe.cropy.providers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.lucaskempe.cropy.services.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object SessionSerializer : Serializer<UserSession> {

    private val gson = Gson()

    override val defaultValue: UserSession
        get() = UserSession()

    override suspend fun readFrom(input: InputStream): UserSession {
        return gson.fromJson(input.reader(), UserSession::class.java)
    }

    override suspend fun writeTo(t: UserSession, output: OutputStream) {
        withContext(Dispatchers.IO + Job()) {
            output.write(gson.toJson(t).toByteArray())
        }
    }

}

val Context.UserSessionDataStore: DataStore<UserSession> by dataStore(
    fileName = "sessions.pb",
    serializer = SessionSerializer
)