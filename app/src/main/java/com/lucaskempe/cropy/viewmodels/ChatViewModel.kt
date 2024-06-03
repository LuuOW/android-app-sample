package com.lucaskempe.cropy.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lucaskempe.cropy.ui.screens.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
): ViewModel() {

    val messages = mutableStateListOf(
        Message("Hello, how are you?", isFromCurrentUser = true, "12:34"),
        Message("I'm good, thanks!", isFromCurrentUser = false, "12:35")
    )

}