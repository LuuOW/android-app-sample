package com.lucaskempe.cropy.services.models.request

data class PasswordResetRequest(
    val phone: String,
    val newPassword: String
)
