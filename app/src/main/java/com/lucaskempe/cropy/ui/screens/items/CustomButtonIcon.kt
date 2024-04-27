package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RemoveIconButton() {
    Icon(
        Icons.Filled.KeyboardArrowDown,
        contentDescription = "Remove", // Accessibility
        modifier = Modifier
            .size(20.dp), // Adjust the size as needed
        tint = Color.Red // Customize the color
    )
}

@Composable
fun IncludeIconButton() {
    Icon(
        Icons.Filled.KeyboardArrowUp,
        contentDescription = "Add", // Accessibility
        modifier = Modifier
            .size(20.dp), // Adjust the size as needed
        tint = Color.Green // Customize the color
    )
}