package detailedProfile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.ProfileError
import components.ProfileHeader
import components.ProfileLoading
import components.ProfileSections
import components.ScreenLayout
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import modifiers.cheapGlassEffect

@OptIn(ExperimentalHazeApi::class)
@Composable
internal fun DetailedProfileScreen(
    modifier: Modifier = Modifier,
    vm: DetailedProfileViewModel,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val hazeState = rememberHazeState()

    ScreenLayout(modifier = modifier) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Animated background
            MovingCirclesBackground(isDarkTheme = isSystemInDarkTheme())

            // Haze source for glass effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState),
            )

            // Glass overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeEffect(
                        state = hazeState,
                        style = HazeStyle(
                            blurRadius = 20.dp,
                            noiseFactor = 0f,
                            tint = HazeTint(
                                color = if (isSystemInDarkTheme()) Color.Black.copy(alpha = 0.05f)
                                else Color.White.copy(alpha = 0.05f),
                            ),
                        ),
                    ),
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                // Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Мой профиль",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                ) {
                    val error = uiState.error
                    val profile = uiState.profile
                    when {
                        uiState.isLoading -> ProfileLoading()
                        error != null -> ProfileError(error)
                        profile != null -> {
                            ProfileHeader(profile)

                            Spacer(Modifier.height(12.dp))

                            ProfileSections(profile)

                            Spacer(Modifier.height(24.dp))

                            // Action buttons with entrance animation
                            var showActions by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                showActions = true
                            }

                            AnimatedVisibility(
                                visible = showActions,
                                enter = fadeIn(animationSpec = spring(dampingRatio = 0.8f)) +
                                        slideInVertically(
                                            animationSpec = spring(dampingRatio = 0.8f),
                                            initialOffsetY = { it / 2 },
                                        ),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    // Logout button
                                    GlassActionButton(
                                        text = "Выйти из аккаунта",
                                        icon = Icons.AutoMirrored.Filled.Logout,
                                        onClick = onLogout,
                                        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )

                                    // Delete account button
                                    GlassActionButton(
                                        text = "Удалить аккаунт",
                                        icon = Icons.Default.DeleteForever,
                                        onClick = { vm.handleIntent(DetailedProfileIntent.DeleteAccount, onDeleteAccount) },
                                        iconTint = MaterialTheme.colorScheme.error,
                                        textColor = MaterialTheme.colorScheme.error,
                                    )
                                }
                            }

                            Spacer(Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    iconTint: Color,
    textColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .cheapGlassEffect(
                shape = RoundedCornerShape(14.dp),
                fillAlpha = 0.1f,
                borderAlpha = 0.15f,
                borderWidth = 0.5.dp,
            )
            .padding(16.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun MovingCirclesBackground(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "profile_bg")

    val x1 by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(20000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c1_x",
    )
    val y1 by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(15000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c1_y",
    )

    val x2 by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(tween(25000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c2_x",
    )
    val y2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(18000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c2_y",
    )

    val color1 = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    val color2 = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
    val bgColor = MaterialTheme.colorScheme.background

    Canvas(modifier = modifier.fillMaxSize().background(bgColor)) {
        val radius1 = size.minDimension * 0.6f
        val radius2 = size.minDimension * 0.5f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color1, Color.Transparent),
                center = Offset(size.width * x1, size.height * y1),
                radius = radius1,
            ),
            center = Offset(size.width * x1, size.height * y1),
            radius = radius1,
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color2, Color.Transparent),
                center = Offset(size.width * x2, size.height * y2),
                radius = radius2,
            ),
            center = Offset(size.width * x2, size.height * y2),
            radius = radius2,
        )
    }
}
