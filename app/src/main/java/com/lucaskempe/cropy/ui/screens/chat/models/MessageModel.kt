package com.lucaskempe.cropy.ui.screens.chat.models


data class MessageModel(
    val text: String,
    val isFromCurrentUser: Boolean,
    val timestamp: String,
    val image: ByteArray = byteArrayOf()
)