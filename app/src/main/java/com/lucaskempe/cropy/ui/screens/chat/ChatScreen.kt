package com.lucaskempe.cropy.ui.screens.chat

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.providers.websocket.WebSocketClient
import com.lucaskempe.cropy.ui.screens.chat.models.MessageModel
import com.lucaskempe.cropy.ui.screens.chat.utils.ChatUtils.formatTimestampToHourMinute
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import com.lucaskempe.cropy.R
import kotlinx.coroutines.launch


@Composable
fun ChatScreen(navController: NavController, padding: PaddingValues) {
    val modifier = Modifier
    val vm: ChatViewModel = hiltViewModel()

    val view = LocalView.current
    var messageText by remember { mutableStateOf("") }
    val hostState = remember { SnackbarHostState() }

    val client = WebSocketClient(state= hostState, urlString = "wss://a474-190-229-153-212.ngrok-free.app/chat?userId=664e8c99a2f3854f5a62caf7", vm = vm, view = view)

    client.connect(vm.context)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) { vm.stateHandle["selectedImageUri"] = it.toString() }
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        modifier = modifier.padding(padding),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Sticker") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = { launcher.launch("image/*") }
            )
        },
        bottomBar = {
            ChatInputBar( // Place ChatInputBar in the bottomBar slot
                messageText = messageText,
                onMessageTextChanged = { messageText = it },
                onSendMessage = { message ->
                    messageText = ""
                    client.sendMessage("664e8c99a2f3854f5a62caf7", "664f6018ec667b41baa87f42", message)
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
            itemsIndexed(vm.messages) { index, message ->
                ChatMessage(message = message)
            }
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    ChatScreen(navController = NavController(LocalContext.current), padding = PaddingValues())
}

