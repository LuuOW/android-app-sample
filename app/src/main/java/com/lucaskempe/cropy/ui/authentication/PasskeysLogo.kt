package com.lucaskempe.cropy.ui.authentication

import android.graphics.PointF
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.plus
import androidx.core.graphics.times
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.lucaskempe.cropy.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PasskeysLogo() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(130.dp) // Adjust size as needed
                .clip(RoundedCornerShape(75.dp)) // Adjust corner radius
                .alpha(0.38f)
                .background(Color.LightGray), // S
        )

        Icon(
            painter = painterResource(id = R.drawable.passkey_24px),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(80.dp), // Adjust icon size within the box
            tint = Color.Black // Optional: Set the icon tint
        )

        Box {
            RoundedPattern()

            Icon(
                painter = painterResource(id = R.drawable.pattern_24px),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp), // Adjust icon size within the box
                tint = Color.White // Optional: Set the icon tint
            )
        }

    }



}

@Composable
fun RoundedPattern() {
    val vertices = remember {
        val radius = 1f
        val innerRadius = .3f
        floatArrayOf(
            radialToCartesian(innerRadius, 0f.toRadians()).x,
            radialToCartesian(innerRadius, 0f.toRadians()).y,
            radialToCartesian(radius, 45f.toRadians()).x,
            radialToCartesian(radius, 45f.toRadians()).y,
            radialToCartesian(innerRadius, 90f.toRadians()).x,
            radialToCartesian(innerRadius, 90f.toRadians()).y,
            radialToCartesian(radius, 135f.toRadians()).x,
            radialToCartesian(radius, 135f.toRadians()).y,
            radialToCartesian(innerRadius, 180f.toRadians()).x,
            radialToCartesian(innerRadius, 180f.toRadians()).y,
            radialToCartesian(radius, 225f.toRadians()).x,
            radialToCartesian(radius, 225f.toRadians()).y,
            radialToCartesian(innerRadius, 270f.toRadians()).x,
            radialToCartesian(innerRadius, 270f.toRadians()).y,
            radialToCartesian(radius, 315f.toRadians()).x,
            radialToCartesian(radius, 315f.toRadians()).y,
        )
    }

    val rounding = remember {
        val roundingNormal = 0.7f
        val roundingNone = 0f
        listOf(
            CornerRounding(roundingNone),    // Vertex 1 (0 degrees) - NO ROUNDING
            CornerRounding(roundingNormal), // Vertex 2 (45 degrees) - ROUNDED
            CornerRounding(roundingNone),    // Vertex 3 (90 degrees) - NO ROUNDING
            CornerRounding(roundingNormal), // Vertex 4 (135 degrees) - ROUNDED
            CornerRounding(roundingNone),    // Vertex 5 (180 degrees) - NO ROUNDING
            CornerRounding(roundingNormal), // Vertex 6 (225 degrees) - ROUNDED
            CornerRounding(roundingNone),    // Vertex 7 (270 degrees) - NO ROUNDING
            CornerRounding(roundingNormal)  // Vertex 8 (315 degrees) - ROUNDED
        )
    }

    val polygon = remember(vertices, rounding) {
        RoundedPolygon(
            vertices = vertices,
            perVertexRounding = rounding
        )
    }
    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygonPath = polygon.toPath().asComposePath()
                onDrawBehind {
                    scale(size.width * 0.3f, size.width * 0.3f) {
                        translate(size.width * 0.5f, size.height * 0.5f) {
                            drawPath(roundedPolygonPath, color = Color(0xFFF15087))
                        }
                    }
                }
            }
            .size(400.dp)
    )
}

internal fun Float.toRadians() = this * PI.toFloat() / 180f

internal val PointZero = PointF(0f, 0f)
internal fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: PointF = PointZero
) = directionVectorPointF(angleRadians) * radius + center

internal fun directionVectorPointF(angleRadians: Float) =
    PointF(cos(angleRadians), sin(angleRadians))

@Composable
@Preview(showBackground = true)
fun PasskeysLogoPreview() {

    PasskeysLogo()

}

