package com.lucaskempe.cropy.providers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.lucaskempe.cropy.services.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object UserSerializer: Serializer<User> {

    private val gson = Gson()

    override val defaultValue: User
        get() = User(
            businessName = "",
            hours = "",
            address = "",
            food = "",
            image = "https://source.android.com/static/docs/setup/images/Android_symbol_green_RGB.png",
            id = 0
        )

    override suspend fun readFrom(input: InputStream): User {
        try {
            return gson.fromJson(input.reader(), User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            return defaultValue
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(gson.toJson(t).toByteArray())
        }
    }

}

val Context.userDataStore: DataStore<User> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer
)