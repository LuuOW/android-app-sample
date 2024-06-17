package com.lucaskempe.cropy.services.session

data class UserSession(
    var id: Int = 0,
    var username: String = "",
    var password: String = "",
    var token: String = ""
)
