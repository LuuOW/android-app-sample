package com.cropy.network.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.Duration
import javax.inject.Inject

class DurationTypeAdapter @Inject constructor() : JsonSerializer<Duration>,
    JsonDeserializer<Duration> {

    private fun toMillis(duration: Duration): Long {
        return duration.toMillis()
    }

    private fun toDuration(millis: Long): Duration {
        return Duration.ofMillis(millis)
    }

    override fun serialize(
        src: Duration?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return if (src == null) {
            JsonNull.INSTANCE
        } else {
            JsonPrimitive(toMillis(src))
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Duration {
        return toDuration(json!!.asLong)
    }
}