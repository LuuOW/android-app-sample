package com.lucaskempe.cropy.ui.screens.chat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucaskempe.cropy.ui.screens.chat.models.MessageModel
import com.lucaskempe.cropy.ui.screens.chat.utils.ChatUtils.formatTimestampToHourMinute
import javax.annotation.meta.When

@Composable
fun ChatMessage(message: MessageModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (message.isFromCurrentUser) Alignment.End else Alignment.Start
    ) {
        MessageBubble(message = message.text, isFromCurrentUser = message.isFromCurrentUser)
        if (message.image.isNotEmpty()) {
            Image(bitmap = message.image.toBitmap(), contentDescription = "image message", modifier = Modifier.size(300.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.timestamp,
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
    }
}

fun ByteArray.toBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size).asImageBitmap()
}

@Preview(showBackground = true)
@Composable
fun ChatMessagePreview() {
    val messageModel = MessageModel("Hey there", false, formatTimestampToHourMinute())
    ChatMessage(message = messageModel)
}