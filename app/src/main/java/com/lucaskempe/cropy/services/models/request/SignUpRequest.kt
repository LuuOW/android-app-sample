package com.lucaskempe.cropy.services.models.request

data class SignUpRequest(
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: String = "USER"
)
