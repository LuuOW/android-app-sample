package com.lucaskempe.cropy.services.maps

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lucaskempe.cropy.services.maps.models.Steps
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
) : ViewModel() {

    var steps by mutableStateOf(arrayListOf(Steps()))
        private set

    fun updateSteps(update: ArrayList<Steps>) {
        steps = update
    }

}