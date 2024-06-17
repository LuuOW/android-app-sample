package com.lucaskempe.cropy.services.models.request

data class PasswordResetRequest(
    val email: String,
    val newPassword: String
)
