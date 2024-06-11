package com.lucaskempe.cropy.ui.screens.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun ChatInputBar(
    messageText: String,
    onMessageTextChanged: (String) -> Unit,
    onSendMessage: (String) -> Unit // Assuming you need recipient ID too
) {
    var recipientId by remember { mutableStateOf("") } // State for recipient ID

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Message Input
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageTextChanged,
            label = { Text("Enter message") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    onSendMessage(messageText)
                }
            )
        )

        // Send Button
        Button(onClick = { onSendMessage(messageText) }) {
            Icon(Icons.Filled.Send, contentDescription = "Send Message")
        }
    }
}