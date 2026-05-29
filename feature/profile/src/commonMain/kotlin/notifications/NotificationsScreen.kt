package notifications

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
import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.ScreenLayout
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import modifiers.cheapGlassEffect
import models.Notification
import models.NotificationType

@OptIn(ExperimentalHazeApi::class)
@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    vm: NotificationsViewModel,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val hazeState = rememberHazeState()

    ScreenLayout(modifier = modifier) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Moving circles background
            MovingCirclesBackground(isDarkTheme = isSystemInDarkTheme())

            // Haze source from content
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
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
            ) {
                // Header with title and refresh
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Уведомления",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f),
                    )

                    if (uiState.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape,
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = "${uiState.unreadCount}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    IconButton(onClick = { vm.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Обновить",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                when {
                    // Loading state
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(48.dp),
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "Загружаем уведомления...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }

                    // Error state
                    uiState.error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(32.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.size(64.dp),
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = uiState.error ?: "Ошибка",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(Modifier.height(24.dp))
                                Box(
                                    modifier = Modifier
                                        .cheapGlassEffect(
                                            shape = RoundedCornerShape(12.dp),
                                            fillAlpha = 0.2f,
                                            borderAlpha = 0.3f,
                                        )
                                        .clickable { vm.refresh() }
                                        .padding(horizontal = 24.dp, vertical = 12.dp),
                                ) {
                                    Text(
                                        text = "Попробовать снова",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        }
                    }

                    // Empty state
                    uiState.notifications.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(32.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                    modifier = Modifier.size(80.dp),
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "Нет уведомлений",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Когда кто-то оценит ваш проект\nили добавит в избранное —\nвы узнаете первыми",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    )
                            }
                        }
                    }

                    // Notifications list
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 16.dp),
                        ) {
                            // Pull-to-refresh indicator at top
                            if (uiState.isRefreshing) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                    }
                                }
                            }

                            itemsIndexed(
                                items = uiState.notifications,
                                key = { _, notification -> notification.id },
                            ) { index, notification ->
                                NotificationCard(
                                    notification = notification,
                                    index = index,
                                    onClick = { /* TODO: открыть связанный контент */ },
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = tween(300),
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: Notification,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Animated entrance with staggered delay
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(index) {
        delay(index * 60L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = spring(dampingRatio = 0.8f, stiffness = 200f)) +
                slideInVertically(
                    animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
                    initialOffsetY = { it / 2 },
                ),
    ) {
        val icon = typeIcon(notification.type)
        val typeColor = iconColor(notification.type)

        Box(
            modifier = modifier
                .fillMaxWidth()
                .cheapGlassEffect(
                    shape = RoundedCornerShape(16.dp),
                    fillAlpha = if (notification.isRead) 0.08f else 0.15f,
                    borderAlpha = if (notification.isRead) 0.1f else 0.25f,
                    borderWidth = if (notification.isRead) 0.5.dp else 1.dp,
                )
                .clickable(onClick = onClick)
                .padding(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                // Type icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(typeColor.copy(alpha = if (notification.isRead) 0.1f else 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = notification.type.displayName,
                        tint = if (notification.isRead) typeColor.copy(alpha = 0.6f) else typeColor,
                        modifier = Modifier.size(22.dp),
                    )
                }

                Spacer(Modifier.width(12.dp))

                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Unread indicator dot
                        if (!notification.isRead) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape,
                                    ),
                            )
                            Spacer(Modifier.width(6.dp))
                        }

                        Text(
                            text = notification.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = notification.content,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(Modifier.height(6.dp))

                    // Timestamp and type badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = formatTimestamp(notification.createdAt),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        )

                        Box(
                            modifier = Modifier
                                .cheapGlassEffect(
                                    shape = RoundedCornerShape(8.dp),
                                    fillAlpha = 0.1f,
                                    borderAlpha = 0.1f,
                                    borderWidth = 0.5.dp,
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                        ) {
                            Text(
                                text = notification.type.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = typeColor.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovingCirclesBackground(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "notif_bg")

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

// ---- Helpers ----

private fun typeIcon(type: NotificationType): ImageVector = when (type) {
    NotificationType.LIKE -> Icons.Default.Favorite
    NotificationType.BOOKMARK -> Icons.Default.Bookmark
    NotificationType.RESPONSE -> Icons.AutoMirrored.Filled.Reply
    NotificationType.MESSAGE -> Icons.AutoMirrored.Filled.Chat
    NotificationType.OTHER -> Icons.Default.MailOutline
}

@Composable
private fun iconColor(type: NotificationType): Color = when (type) {
    NotificationType.LIKE -> Color(0xFFE91E63)   // Pink
    NotificationType.BOOKMARK -> Color(0xFFFF9800) // Amber
    NotificationType.RESPONSE -> Color(0xFF4CAF50) // Green
    NotificationType.MESSAGE -> Color(0xFF2196F3)  // Blue
    NotificationType.OTHER -> MaterialTheme.colorScheme.primary
}

private fun formatTimestamp(createdAt: String?): String {
    if (createdAt == null) return ""
    // Try to extract just the date portion from ISO datetime
    return try {
        val datePart = createdAt.take(10) // "2024-01-15"
        val parts = datePart.split("-")
        if (parts.size == 3) {
            val months = listOf(
                "янв", "фев", "мар", "апр", "мая", "июн",
                "июл", "авг", "сен", "окт", "ноя", "дек",
            )
            val monthIndex = parts[1].toIntOrNull()?.minus(1) ?: return datePart
            "${parts[2].toIntOrNull() ?: parts[2]} ${months.getOrElse(monthIndex) { parts[1] }}"
        } else {
            createdAt
        }
    } catch (_: Exception) {
        createdAt
    }
}
