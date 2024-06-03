package com.lucaskempe.cropy.ui.screens

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lucaskempe.cropy.providers.websocket.WebSocketClient
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ChatScreen(navController: NavController, padding: PaddingValues) {
    val modifier = Modifier
    val vm: ChatViewModel = hiltViewModel()

    var messageText by remember { mutableStateOf("") }

    val client = WebSocketClient(
        urlString = "ws://192.168.1.2:8082/chat?userId=user1",
        onMessageReceived = {
            vm.messages.add(Message(it.messageContent, false, formatTimestampToHourMinute()))
            Log.d("messsage", "${it.messageContent}")
        },
        onConnectionOpened = {
            Log.d("message", "Connection opened")
        },
        onConnectionClosed = { p1, p2 ->
            Log.d("message", "Connection closed: $p1, $p2")
        },
        onConnectionFailed = {
            Log.d("message", "Connection failed")
            throw Exception(it)
        },
        vm = vm
    )

    client.connect()

    Scaffold(
        modifier = modifier.padding(padding),
        bottomBar = {
            ChatInputBar( // Place ChatInputBar in the bottomBar slot
                messageText = messageText,
                onMessageTextChanged = { messageText = it },
                onSendMessage = { message ->
                    messageText = ""
                    client.sendMessage("user3", message)
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Apply innerPadding to the content
            reverseLayout = false,
        ) {
            items(vm.messages) { message ->
                ChatMessage(message = message)
            }
        }
    }
}

fun formatTimestampToHourMinute(): String {
    val timestamp = System.currentTimeMillis()
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}


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

@Composable
fun ChatMessage(message: Message) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (message.isFromCurrentUser) Alignment.End else Alignment.Start
    ) {
        MessageBubble(message = message.text, isFromCurrentUser = message.isFromCurrentUser)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.timestamp,
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
    }
}

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

@Preview
@Composable
fun ChatScreenPreview() {
    //ChatScreen(navController = NavController(LocalContext.current))
}

data class Message(
    val text: String,
    val isFromCurrentUser: Boolean,
    val timestamp: String
)
