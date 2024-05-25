package com.lucaskempe.cropy.services.models

data class ImageResponse(
    val id: Int? = null,
    val fileId: String,
    val filename: String,
    val metadata: Map<String, String>? = null
)
