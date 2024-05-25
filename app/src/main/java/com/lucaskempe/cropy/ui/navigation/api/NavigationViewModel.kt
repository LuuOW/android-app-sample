package com.lucaskempe.cropy.ui.navigation.api

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val destSet = mutableSetOf<DestinationObject>()
}