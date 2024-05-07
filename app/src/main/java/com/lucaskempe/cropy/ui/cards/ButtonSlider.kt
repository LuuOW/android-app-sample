package com.lucaskempe.cropy.ui.cards

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ButtonSlider(

) {
    val modifier = Modifier
    val scrollState = rememberScrollState()
    Row(
        modifier.horizontalScroll(scrollState).padding(12.dp)
    ) {
        repeat(buttonSliderData.size) {
            var enabled by remember { mutableStateOf(false) }
            val data = buttonSliderData[it]
            Button(
                colors = ButtonColors(
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.Black,
                    containerColor = if (enabled) Color(43,43,70) else Color.White,
                    contentColor = if (enabled) Color.White else Color.Black
                ),
                border =  ButtonDefaults.outlinedButtonBorder,
                elevation = ButtonDefaults.buttonElevation(7.dp),
                modifier = modifier
                    .padding(4.dp),
                shape = MaterialTheme.shapes.small,
                onClick = { enabled = !enabled }
            )
            {
                Icon(data.icon, contentDescription = null)
                Spacer(modifier = modifier.padding(4.dp))
                Text(data.text, modifier = modifier, style = MaterialTheme.typography.labelMedium)

            }
        }
    }

}

data class ButtonSliderData(
    val text: String,
    val icon: ImageVector
)

val buttonSliderData = listOf(
    ButtonSliderData("Compras", Icons.Filled.ShoppingCart),
    ButtonSliderData("Promociones", Icons.Filled.Star),
    ButtonSliderData("Cerca de mi", Icons.Filled.LocationOn),
)

@Preview
@Composable
fun ButtonSliderPreview() {
    ButtonSlider()
}