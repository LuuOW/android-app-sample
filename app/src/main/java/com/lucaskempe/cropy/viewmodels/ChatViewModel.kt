package com.lucaskempe.cropy.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.lucaskempe.cropy.ui.screens.chat.models.MessageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val stateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
): ViewModel() {

    val selectedImageUri: StateFlow<String> = stateHandle.getStateFlow("selectedImageUri", "")

    val messages = mutableStateListOf(
        MessageModel("Hello, how are you?", isFromCurrentUser = true, "12:34"),
        MessageModel("I'm good, thanks!", isFromCurrentUser = false, "12:35")
    )

}