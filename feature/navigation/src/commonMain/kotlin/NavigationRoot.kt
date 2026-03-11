import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun NavigationRoot(modifier: Modifier = Modifier) {
    val rootBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.OnBoarding::class, Route.OnBoarding.serializer())
                    subclass(Route.Auth::class, Route.Auth.serializer())
                    subclass(Route.Error::class, Route.Error.serializer())
                }
            }
        }, Route.OnBoarding
    )
    Scaffold {
        Background(enabled = true, useHexagonBackground = true, painter = null) {
            NavDisplay(
                modifier = modifier, backStack = rootBackStack, entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ), entryProvider = entryProvider {
                    entry<Route.OnBoarding> {
                        OnBoardingNavigation()
                    }
                })
        }
    }
}


@Composable
private fun Background(
    enabled: Boolean,
    painter: Painter? = null,
    useHexagonBackground: Boolean = false,
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    when {
        enabled && useHexagonBackground -> {
            HexagonGradientBackground(
                isDarkTheme = isDarkTheme
            ) {
                content()
            }
        }

        enabled && painter != null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                content()
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun HexagonGradientBackground(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val lightGradientColors = listOf(
        Color(0xFFFF6B9D),
        Color(0xFFC44569),
        Color(0xFF4A90E2)
    )

    val darkGradientColors = listOf(
        Color(0xFF2D1B4E),
        Color(0xFF1A1A2E),
        Color(0xFF0F0F1E)
    )

    val lightHexagonColor = Color.White.copy(alpha = 0.08f)
    val darkHexagonColor = Color.White.copy(alpha = 0.05f)

    // Анимация для каждого шестиугольника
    val infiniteTransition = rememberInfiniteTransition(label = "hexagonFloat")

    // Параметры анимации для 6 шестиугольников
    val hex1OffsetX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex1X"
    )
    val hex1OffsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex1Y"
    )

    val hex2OffsetX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex2X"
    )
    val hex2OffsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 35f,
        animationSpec = infiniteRepeatable(
            animation = tween(6500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex2Y"
    )

    val hex3OffsetX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 35f,
        animationSpec = infiniteRepeatable(
            animation = tween(5500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex3X"
    )
    val hex3OffsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(7500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex3Y"
    )

    val hex4OffsetX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(6500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex4X"
    )
    val hex4OffsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(5500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex4Y"
    )

    val hex5OffsetX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex5X"
    )
    val hex5OffsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 35f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex5Y"
    )

    val hex6OffsetX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex6X"
    )
    val hex6OffsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hex6Y"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = if (isDarkTheme) darkGradientColors else lightGradientColors,
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val hexagonColor = if (isDarkTheme) darkHexagonColor else lightHexagonColor

            // Шестиугольники с анимированными позициями
            drawHexagon(
                centerX = size.width * 0.15f + hex1OffsetX.value,
                centerY = size.height * 0.2f + hex1OffsetY.value,
                radius = 80.dp.toPx(),
                color = hexagonColor
            )

            drawHexagon(
                centerX = size.width * 0.75f + hex2OffsetX.value,
                centerY = size.height * 0.15f + hex2OffsetY.value,
                radius = 70.dp.toPx(),
                color = hexagonColor
            )

            drawHexagon(
                centerX = size.width * 0.25f + hex3OffsetX.value,
                centerY = size.height * 0.5f + hex3OffsetY.value,
                radius = 90.dp.toPx(),
                color = hexagonColor
            )

            drawHexagon(
                centerX = size.width * 0.7f + hex4OffsetX.value,
                centerY = size.height * 0.55f + hex4OffsetY.value,
                radius = 75.dp.toPx(),
                color = hexagonColor
            )

            drawHexagon(
                centerX = size.width * 0.2f + hex5OffsetX.value,
                centerY = size.height * 0.85f + hex5OffsetY.value,
                radius = 65.dp.toPx(),
                color = hexagonColor
            )

            drawHexagon(
                centerX = size.width * 0.8f + hex6OffsetX.value,
                centerY = size.height * 0.8f + hex6OffsetY.value,
                radius = 85.dp.toPx(),
                color = hexagonColor
            )
        }
        content()
    }
}

private fun DrawScope.drawHexagon(
    centerX: Float,
    centerY: Float,
    radius: Float,
    color: Color
) {
    val points = mutableListOf<Offset>()
    for (i in 0 until 6) {
        val angle = (60 * i - 30) * PI / 180.0
        val x = (centerX + radius * cos(angle)).toFloat()
        val y = (centerY + radius * sin(angle)).toFloat()
        points.add(Offset(x, y))
    }
    drawPath(
        path = Path().apply {
            moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
            close()
        },
        color = color
    )
}