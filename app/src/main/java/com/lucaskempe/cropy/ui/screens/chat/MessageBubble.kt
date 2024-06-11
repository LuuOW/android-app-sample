package com.lucaskempe.cropy.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MessageBubble(message: String, isFromCurrentUser: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (isFromCurrentUser) MaterialTheme.colors.primary else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.body1,
            color = if (isFromCurrentUser) Color.White else Color.Black
        )
    }
}