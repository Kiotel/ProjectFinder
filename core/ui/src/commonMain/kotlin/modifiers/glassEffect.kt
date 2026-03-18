package modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glassEffect(
    shape: Shape = RectangleShape,
    baseColor: Color = Color.White,
    fillAlpha: Float = 0.25f,
    borderAlpha: Float = 0.5f,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(color = baseColor.copy(alpha = fillAlpha), shape = shape)
    .border(width = borderWidth, color = baseColor.copy(alpha = borderAlpha), shape = shape)