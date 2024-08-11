package com.cropy.entities.network.response

data class ImageResponse(
    val id: Int? = null,
    val fileId: String,
    val filename: String,
    val metadata: Map<String, String>? = null
)
