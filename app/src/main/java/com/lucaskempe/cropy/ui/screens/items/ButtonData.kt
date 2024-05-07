package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset

data class ButtonData(
    val id: Int,
    val icon: String = "https://png.pngtree.com/png-vector/20190820/ourmid/pngtree-no-image-vector-illustration-isolated-png-image_1694547.jpg",
    val title: String,
    val urlAction: String,
    var offSet: MutableState<IntOffset> = mutableStateOf(IntOffset(0,0)),
    var originalOffset: MutableState<IntOffset> = mutableStateOf(IntOffset(0,0)),
    var enabled: Boolean = false
)
