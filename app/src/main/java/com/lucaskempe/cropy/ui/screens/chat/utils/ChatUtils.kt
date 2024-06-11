package com.lucaskempe.cropy.ui.screens.chat.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ChatUtils {

    fun formatTimestampToHourMinute(): String {
        val timestamp = System.currentTimeMillis()
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

}