package com.cropy.entities.network.request

data class SMSVerifyRequest(
    val phone: String,
    val code: String,
)
