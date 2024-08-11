package com.cropy.network.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class InstantTypeAdapter @Inject constructor(): JsonSerializer<Instant>, JsonDeserializer<Instant> {

    private fun toIsoString(date: Instant): String {
        return DateTimeFormatter.ISO_INSTANT.format(date)
    }

    private fun toInstant(iso: String): Instant {
        return Instant.parse(iso)
    }

    override fun serialize(
        src: Instant?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return if (src == null || src == Instant.MIN) {
            JsonNull.INSTANCE
        } else {
            JsonPrimitive(toIsoString(src))
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instant {
        return if (json == null || json == JsonNull.INSTANCE) {
            Instant.MIN
        } else {
            toInstant(json.asString)
        }
    }
}