package com.cropy.entities.network.request

data class PasswordResetRequest(
    val phone: String,
    val newPassword: String
)
