package com.lucaskempe.cropy.services.models.request

data class SMSVerifyRequest(
    val phone: String,
    val code: String,
)
