package com.lucaskempe.cropy.ui.screens.chat

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.R
import com.lucaskempe.cropy.ui.screens.chat.models.MessageModel
import com.lucaskempe.cropy.ui.screens.chat.utils.ChatUtils.formatTimestampToHourMinute
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ChatScreen(navController: NavController, padding: PaddingValues, vm: ChatViewModel) {
    val modifier = Modifier
    val view = LocalView.current
    var messageText by remember { mutableStateOf("") }
    val hostState = remember { SnackbarHostState() }
    val wssUrl = BuildConfig.WSS_BASE_URL

    var uri by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(vm.errorMessage) {
        if (vm.errorMessage.value) {
            Snackbar
                .make(view, "WSS Connection Failed", Snackbar.LENGTH_SHORT)
                .setAction("Retry") { vm.webSocketClient.connect(wssUrl) }
                .setActionTextColor(ContextCompat.getColor(vm.context, R.color.white))
                .setBackgroundTint(ContextCompat.getColor(vm.context, R.color.notred))
                .show()
            vm.errorMessage.emit(false)
        }
    }

    LaunchedEffect(Unit) {
        vm.webSocketClient.connect(wssUrl)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                uri = it.toString()
                showDialog = true
            }
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
                    vm.webSocketClient.sendMessage("sqluser4", "sqluser1", message)
                    vm.messages.add(MessageModel(message, isFromCurrentUser = true, formatTimestampToHourMinute()))
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
                AnimatedVisibility(visible = true) {
                    ChatMessage(message = message)
                }
            }
        }
        if (showDialog) {
            ImageMessageDialog(uri=Uri.parse(uri), context = vm.context,
                onMessageSent = {
                    val inputStream = vm.context.contentResolver.openInputStream(Uri.parse(uri))
                    val bytes = inputStream?.readBytes()
                    val base64Image = bytes?.let { it1 -> Base64.encode(it1) }!!
                    vm.webSocketClient.sendEncodedMessage(base64Image,"sqluser1", "sqluser4", "hello there" )
                    showDialog = false
                },
                onDismissRequest = { showDialog = false })
        }
    }
}


@Composable
fun ImageMessageDialog(
    uri: Uri,
    onDismissRequest: () -> Unit = { },
    onMessageSent: () -> Unit = { },
    context: Context
) {
    val bitmap = remember {
        val inputStream = context.contentResolver.openInputStream(uri)
        val decodedBitmap = if (inputStream != null) {
            BitmapFactory.decodeStream(inputStream)
        } else {
            // Handle error, e.g., show a placeholder image
            null
        }
        decodedBitmap?.asImageBitmap()
    }

    AlertDialog(
        modifier = Modifier.size(300.dp),
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Preview your message")
        },
        text = {
            Column(modifier = Modifier) {
/*                TextField(
                    value = "",
                    onValueChange = { onMessageSent(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Enter message") }
                )*/

                Image(bitmap = bitmap!!, contentDescription = null, modifier = Modifier.size(400.dp))

            }
        },
        confirmButton = {
            Button(onClick = { onMessageSent() }) {
                Text("Send")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun ChatScreenPreview() {
    val vm: ChatViewModel = hiltViewModel()
    ChatScreen(navController = NavController(LocalContext.current), padding = PaddingValues(), vm)
}

