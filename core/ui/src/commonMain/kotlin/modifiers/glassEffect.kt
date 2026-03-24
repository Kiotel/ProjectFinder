package modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun Modifier.glassEffect(
    hazeState: HazeState,
    shape: Shape = RectangleShape,
    tint: Color = if (isSystemInDarkTheme()) Color.Black else Color.White,
    blurRadius: Dp = 20.dp,
    fillAlpha: Float = 0.15f,
    borderAlpha: Float = 0.2f,
    borderWidth: Dp = 1.dp
): Modifier = this.clip(shape).hazeEffect(
    state = hazeState, style = HazeStyle(
        blurRadius = blurRadius,
        tints = listOf(HazeTint(tint.copy(alpha = fillAlpha))),
    )
).border(
    width = borderWidth, color = tint.copy(alpha = borderAlpha), shape = shape
)

@Composable
fun Modifier.cheapGlassEffect(
    shape: Shape = RectangleShape,
    tint: Color = if (isSystemInDarkTheme()) Color.Black else Color.White,
    fillAlpha: Float = 0.15f,
    borderAlpha: Float = 0.2f,
    borderWidth: Dp = 1.dp
): Modifier = this.clip(shape).background(
    color = tint.copy(alpha = fillAlpha), shape = shape
).border(
    width = borderWidth, color = tint.copy(alpha = borderAlpha), shape = shape
)