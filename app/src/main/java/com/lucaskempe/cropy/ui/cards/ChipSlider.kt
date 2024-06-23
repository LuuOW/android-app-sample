package com.lucaskempe.cropy.ui.cards

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChipSlider() {

    val modifier = Modifier
    val scrollState = rememberScrollState()
    Row(
        modifier
            .horizontalScroll(scrollState)
            .padding(12.dp)
    ) {
        repeat(buttonSliderData.size) {
            FilterChipExample(data = buttonSliderData1[it])
        }
    }

}

@Composable
fun FilterChipExample(
    data: ButtonSliderData
) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        modifier = Modifier.padding(horizontal = 6.dp),
        onClick = { selected = !selected },
        label = {
            Text(data.text)
        },
        selected = selected,
        trailingIcon = {
            Icon(
                imageVector = data.icon,
                contentDescription = "Action icon",
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

val buttonSliderData1 = listOf(
    ButtonSliderData("Compras", Icons.Filled.ShoppingCart),
    ButtonSliderData("Promociones", Icons.Filled.Star),
    ButtonSliderData("Cerca de mi", Icons.Filled.LocationOn),
)

@Preview
@Composable
fun ChipSliderPreview() {
    ChipSlider()
}