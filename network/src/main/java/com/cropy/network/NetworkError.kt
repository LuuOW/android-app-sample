package com.cropy.network

import com.google.gson.annotations.SerializedName

data class NetworkError(
    @SerializedName("detail")
    val detail: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("data")
    val data: NetworkErrorDataWrapped?,
)

data class NetworkErrorDataWrapped(
    @SerializedName("message")
    val message: String?,
)
