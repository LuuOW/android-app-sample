package com.cropy.entities.user

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    var id: Int = 0,
    var username: String = "",
    var password: String = "",
    var token: String = ""
)
