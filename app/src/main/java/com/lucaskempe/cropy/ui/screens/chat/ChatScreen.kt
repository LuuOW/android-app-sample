package com.lucaskempe.cropy.ui.screens.chat

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.rememberImagePainter
import com.google.android.material.snackbar.Snackbar
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.providers.websocket.WebSocketClient
import com.lucaskempe.cropy.ui.screens.chat.models.MessageModel
import com.lucaskempe.cropy.ui.screens.chat.utils.ChatUtils.formatTimestampToHourMinute
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import com.lucaskempe.cropy.R
import com.lucaskempe.cropy.services.workers.CodeVerifyWorker
import com.lucaskempe.cropy.services.workers.ImageUploadWorker
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ChatScreen(navController: NavController, padding: PaddingValues) {
    val modifier = Modifier
    val vm: ChatViewModel = hiltViewModel()

    val view = LocalView.current
    var messageText by remember { mutableStateOf("") }
    val hostState = remember { SnackbarHostState() }
    val workManager: WorkManager = WorkManager.getInstance(vm.context)

    var uri by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val client = WebSocketClient(state= hostState, vm = vm, view = view)

    client.connect(vm.context)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                uri = it.toString()
                showDialog = true
/*                val workRequest = OneTimeWorkRequest.Builder(ImageUploadWorker::class.java)
                    .setInputData(
                        workDataOf(
                            "image" to it.toString(),
                            "filename" to "image.jpg",
                            "userId" to "6670b2a9cacd892d4c9051be"
                        )
                    )
                    .build()
                workManager.enqueueUniqueWork(
                    "one_time",
                    ExistingWorkPolicy.KEEP,
                    workRequest
                )*/


/*                val inputStream = vm.context.contentResolver.openInputStream(it)
                val bytes = inputStream?.readBytes()
                val base64Image = bytes?.let { it1 -> Base64.encode(it1) }!!

                client.sendEncodedMessage(base64Image,"6670b2a9cacd892d4c9051be", "6671c5ea7021623ec9e7d084", "hello there" )*/

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
                    client.sendMessage("sqluser1", "sqluser2", message)
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
        if (showDialog) {
            ImageMessageDialog(uri=Uri.parse(uri), context = vm.context,
                onMessageSent = {
                    val inputStream = vm.context.contentResolver.openInputStream(Uri.parse(uri))
                    val bytes = inputStream?.readBytes()
                    val base64Image = bytes?.let { it1 -> Base64.encode(it1) }!!
                    client.sendEncodedMessage(base64Image,"sqluser1", "sqluser4", "hello there" )
                    showDialog = false
                },
                onDismissRequest = {
                showDialog = false
            })
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
    ChatScreen(navController = NavController(LocalContext.current), padding = PaddingValues())
}

