package com.lucaskempe.cropy.ui.authentication

import android.graphics.ColorSpace.Rgb
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
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
        modifier = Modifier.size(300.dp)
    ) {

        Box(
            modifier = Modifier
                .padding(start = 200.dp, bottom = 60.dp)
                .align(Alignment.Center)
        ) {
            LinesFromBottomLeft()
        }

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

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 180.dp, end = 90.dp)
                .alpha(.99f)
                .scale(scaleX = .7f, scaleY = .7f)
        ) {
            RoundedPattern()
        }


        Box(
            modifier = Modifier
                .padding(end = 150.dp, top = 40.dp)
                .align(Alignment.Center)
                .scale(scaleY = .7f, scaleX = .7f)
                .size(100.dp) // Adjust size as needed
                .clip(CircleShape)
                .alpha(.99f)
                .background(Color(0xFF8EA0B3))
        ) {
            FaceIcon()
        }

        Box(
            modifier = Modifier
                .padding(start = 180.dp, top = 20.dp)
                .align(Alignment.Center)
        ) {
            FingerPrintIcon()
        }

    }



}

@Composable
fun LinesFromBottomLeft(
    color: Color = Color.Black, // Color of the lines
    size: Int = 100, // Size of the "invisible" square
    strokeWidth: Float = 3f, // Width of the lines
    shorteningFactor: Float = 0.8f // Factor to shorten the outer lines (0.0f - 1.0f)
) {
    Canvas(modifier = Modifier.size(size.dp)) {
        val bottomLeft = Offset(0f, size.toFloat()) // Bottom-left corner of the Canvas
        val angles = listOf(270f, 315f, 0f) // Angles for the lines

        for ((index, angle) in angles.withIndex()) { // Use withIndex for the index
            val radians = Math.toRadians(angle.toDouble()).toFloat()

            val lineLength = if (index == 1) size.toFloat() else size.toFloat() * shorteningFactor
            val endX = bottomLeft.x + lineLength * cos(radians)  // Line endpoint calculation
            val endY = bottomLeft.y + lineLength * sin(radians)

            val endPoint = Offset(endX, endY)

            drawLine(
                color = color,
                start = bottomLeft,
                end = endPoint,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round // Optional: for rounded line ends
            )
        }

        // Face outline
        drawCircle(
            color = Color.White,
            center = bottomLeft,
            radius = 30f,
            style = Fill // Adjust stroke width as needed
        )
    }


}


@Composable
fun FingerPrintIcon() {
    val vertices = remember {
        val radius = 1f
        floatArrayOf(
            radialToCartesian(radius, 45f.toRadians()).x,
            radialToCartesian(radius, 45f.toRadians()).y,
            radialToCartesian(radius, 135f.toRadians()).x,
            radialToCartesian(radius, 135f.toRadians()).y,
            radialToCartesian(radius, 225f.toRadians()).x,
            radialToCartesian(radius, 225f.toRadians()).y,
            radialToCartesian(radius, 315f.toRadians()).x,
            radialToCartesian(radius, 315f.toRadians()).y,
        )
    }

    val rounding = remember {
        val roundingNormal = 0.7f
        val roundingNone = 0f
        listOf(
            CornerRounding(roundingNormal),    // Vertex 1 (0 degrees) - NO ROUNDING
            CornerRounding(roundingNone), // Vertex 2 (45 degrees) - ROUNDED
            CornerRounding(roundingNormal),    // Vertex 3 (90 degrees) - NO ROUNDING
            CornerRounding(roundingNormal), // Vertex 4 (135 degrees) - ROUNDED
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
                    scale(size.width * 0.35f, size.width * 0.35f) {
                        translate(size.width * 0.5f, size.height * 0.5f) {
                            drawPath(
                                path = roundedPolygonPath,
                                color = Color.Black,
                                style = Stroke(width = .025f)
                            )
                        }
                    }
                }
            }
            .size(400.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.fingerprint_24px),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp), // Adjust icon size within the box
            tint = Color.Black // Optional: Set the icon tint
        )
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
                    scale(size.width * 0.8f, size.width * 0.8f) {
                        translate(size.width * 0.5f, size.height * 0.5f) {
                            drawPath(roundedPolygonPath, color = Color(red = 150, green =212 ,199))
                        }
                    }
                }
            }
            .size(400.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.pattern_24px),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp), // Adjust icon size within the box
            tint = Color.White // Optional: Set the icon tint
        )
    }


}

@Composable
fun FaceIcon(color: Color = Color.Black, size: Int = 280) {
    Canvas(modifier = Modifier.size(size.dp)) {
        val center = Offset(size / 2f, size / 2f)
        val radius = size / 3f

        // Face outline
        drawCircle(
            color = color,
            center = center,
            radius = radius,
            style = Stroke(width = 2f) // Adjust stroke width as needed
        )

        // Eyes
        val eyeRadius = 5f
        val leftEyeCenter = Offset(center.x - radius / 2.5f, center.y - radius / 4f)
        val rightEyeCenter = Offset(center.x + radius / 2.5f, center.y - radius / 4f)

        drawCircle(color = color, center = leftEyeCenter, radius = eyeRadius)
        drawCircle(color = color, center = rightEyeCenter, radius = eyeRadius)

        // Nose (L-shape)
        val nosePath = Path().apply {
            moveTo(center.x, center.y + radius / 6f)
            lineTo(center.x + radius / 8f, center.y + radius / 2f)
            lineTo(center.x, center.y + radius / 2f)
        }
        drawPath(
            path = nosePath,
            color = color,
            style = Stroke(width = 2f, cap = StrokeCap.Round) // Rounded nose tip
        )


        // smile (Arc)
        drawArc(
            color = color,
            startAngle = 90f,
            sweepAngle = 40f,
            useCenter = false, // Don't connect the arc ends to the center
            topLeft = Offset(center.x - radius / 2f, center.y - 30f),
            size = androidx.compose.ui.geometry.Size(radius, radius),
            style = Stroke(width = 2f, cap = StrokeCap.Round) // Rounded smile ends
        )
    }
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

