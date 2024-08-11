package com.cropy.components.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cropy.components.colors.AppColor
import com.cropy.components.fonts.AppTypography
import kotlin.math.roundToInt

@Composable
fun InstructionsBar(
    instructions: String = "Head North Towards Md St",
    maneuver: String = "STRAIGHT",
    distance: String = "2564"
) {

    val modifier = Modifier
    Surface(
        modifier = Modifier
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AppColor.Blue900)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp, top = 20.dp)
                    .height(128.dp)
            ) {
                Row (
                ){

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        Image(
                            colorFilter = ColorFilter.tint(AppColor.White),
                            imageVector =  when(maneuver) {
                                "TURN_LEFT" -> Icons.AutoMirrored.Filled.ArrowBack
                                "TURN_RIGHT" -> Icons.AutoMirrored.Filled.ArrowForward
                                "STRAIGHT" -> Icons.Filled.KeyboardArrowUp
                                "ARRIVED" -> Icons.Filled.Place
                                else -> Icons.Filled.KeyboardArrowUp
                            },
                            contentDescription = "maneuver"
                        )

                        Spacer(modifier = Modifier.padding(4.dp))

                        Text(
                            text = metersToMilesOrFeet(distance),
                            style = AppTypography.bodyXL.copy(
                                fontSize = 24.sp,
                                color = AppColor.White
                            ),
                            textAlign = TextAlign.Left,
                        )
                    }
                    Spacer(modifier = Modifier.padding(14.dp))

                    Column {
                        Text(
                            text = instructions ?: "Head North Towards Md St",
                            style = AppTypography.bodyL.copy(
                                fontSize = 24.sp,
                                color = AppColor.White
                            ),
                            textAlign = TextAlign.Left,
                        )

                        Spacer(modifier = Modifier.padding(4.dp))

                        Text(
                            text =  when(maneuver) {
                                "TURN_LEFT" -> "Turn Left"
                                "TURN_RIGHT" -> "Turn Right"
                                "STRAIGHT" -> "Keep Straight"
                                "ARRIVED" -> "You just arrived"
                                else -> maneuver
                            },
                            style = AppTypography.bodyM
                        )
                    }
                }
            }
        }
    }
}

fun metersToMilesOrFeet(distance: String): String {
    try {
        // Convert distance string to float
        val distanceMeters = distance.toFloatOrNull() ?: return ""

        // Check for negative distance
        if (distanceMeters < 0) {
            return "Distance cannot be negative."
        }

        // Conversion factors
        val metersPerFoot = 0.3048f
        val metersPerMile = 1609.34f

        // Convert to feet or miles based on the distance
        val convertedValue: String = if (distanceMeters < metersPerMile) {
            "${(distanceMeters * metersPerFoot).roundToInt()} ft"
        } else {
            "%.2f mi".format(distanceMeters / metersPerMile)
        }

        return convertedValue
    } catch (e: NumberFormatException) {
        return "Invalid distance format (must be a number)."
    }
}

@Preview(showBackground = false)
@Composable
fun InstructionsBarPreview() {
    InstructionsBar("Head North Towards Md St", "ARRIVED", "1783")
}