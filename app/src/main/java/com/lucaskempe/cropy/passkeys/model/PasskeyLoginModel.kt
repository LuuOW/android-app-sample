package com.lucaskempe.cropy.passkeys.model

data class PasskeyLoginModel(
    val challenge: String,
    val allowCredentials: List<String>,
    val timeout: Int,
    val userVerification: String,
    val rpId: String
)
