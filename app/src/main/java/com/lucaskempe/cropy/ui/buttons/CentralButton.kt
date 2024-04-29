package com.lucaskempe.cropy.ui.buttons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun CentralButton(
    modifier: Modifier,
    navController: NavController
) {
    Box(
        modifier=modifier
    ) {
        Canvas(
            modifier = modifier
                .clickable {
                    navController.navigate("inspector")
                }
                .size(35.dp)
                .align(Alignment.Center)
                .padding(bottom = 20.dp)
                .windowInsetsPadding(WindowInsets.displayCutout)
        ) {
            drawCircle(
                color = Color(70,156,221),
                radius = 35.dp.toPx()
            )
        }
    }
}