package com.lucaskempe.cropy.providers.websocket

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lucaskempe.cropy.ui.screens.chat.models.MessageModel
import com.lucaskempe.cropy.ui.screens.chat.utils.ChatUtils.formatTimestampToHourMinute
import com.lucaskempe.cropy.viewmodels.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebSocketReceiver(
    private val chatViewModel: ChatViewModel
): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when(intent.action) {
            "ACTION_MESSAGE_RECEIVED" -> {
                val message = intent.getStringExtra("MESSAGE")
                if (message != null) {
                    chatViewModel.messages.add(MessageModel(message, isFromCurrentUser = false, timestamp = formatTimestampToHourMinute()))
                }
            }
            "ACTION_BASE64_MESSAGE_RECEIVED" -> {
                val image = intent.getByteArrayExtra("BASE64_IMAGE")
                if (image != null) {
                    chatViewModel.messages.add(MessageModel("image", isFromCurrentUser = false, timestamp = formatTimestampToHourMinute(), image))
                }
            }
            "ERROR" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    chatViewModel.errorMessage.emit(true)
                }
            }
        }
    }

}