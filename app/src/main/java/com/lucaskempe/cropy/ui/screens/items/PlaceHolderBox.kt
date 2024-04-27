package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlaceHolderBox(
    modifier: Modifier
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 15.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White,),
        shape = AbsoluteRoundedCornerShape(24.dp),
        modifier = modifier
            .padding(8.dp)
            .size(80.dp)
    ) {
        Text(text = "Drop Box")
    }
}