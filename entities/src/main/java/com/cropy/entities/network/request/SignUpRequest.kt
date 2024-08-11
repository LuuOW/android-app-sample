package com.cropy.entities.network.request

data class SignUpRequest(
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: String = "USER"
)
