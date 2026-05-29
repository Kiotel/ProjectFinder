package allProjects

import allProjects.models.AllProjectsState
import allProjects.models.ProjectFilter
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import components.ScreenLayout
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import models.Project
import models.ProjectStage
import modifiers.cheapGlassEffect
import utils.SnackBarManager

@OptIn(ExperimentalHazeApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AllProjectsScreen(
    modifier: Modifier = Modifier,
    pagingFlow: Flow<PagingData<Project>>,
    uiState: AllProjectsState,
    handleIntent: (intent: AllProjectsIntent) -> Unit,
    snackBarManager: SnackBarManager,
    goToProject: (project: Project) -> Unit,
    goToCreate: () -> Unit,
) {
    val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()
    val hazeState = rememberHazeState()

    ScreenLayout(modifier = modifier, snackBarManager = snackBarManager) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading,
            onRefresh = { 
                lazyPagingItems.refresh() 
                handleIntent(AllProjectsIntent.Refresh)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Animated background
                MovingCirclesBackground(isDarkTheme = isSystemInDarkTheme())

            // Haze source
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
                // Search bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .cheapGlassEffect(
                            shape = RoundedCornerShape(24.dp),
                            fillAlpha = 0.15f,
                            borderAlpha = 0.15f,
                            borderWidth = 0.5.dp,
                        ),
                ) {
                    TextField(
                        value = uiState.searchQuery,
                        onValueChange = { handleIntent(AllProjectsIntent.UpdateSearchQuery(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Поиск проектов...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = Transparent,
                            focusedContainerColor = Transparent,
                            focusedIndicatorColor = Transparent,
                            unfocusedIndicatorColor = Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        ),
                    )
                }

                // Filter tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProjectFilter.entries.forEach { filter ->
                        val isActive = uiState.filter == filter
                        val label = when (filter) {
                            ProjectFilter.ALL -> "Все"
                            ProjectFilter.MINE -> "Мои"
                            ProjectFilter.JOINED -> "Участвую"
                        }
                        Box(
                            modifier = Modifier
                                .cheapGlassEffect(
                                    shape = RoundedCornerShape(20.dp),
                                    fillAlpha = if (isActive) 0.3f else 0.08f,
                                    borderAlpha = if (isActive) 0.3f else 0.1f,
                                    tint = if (isActive) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                .clickable { handleIntent(AllProjectsIntent.SetFilter(filter)) }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isActive) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Projects list
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        start = 12.dp, end = 12.dp, top = 4.dp, bottom = 88.dp
                    ),
                ) {
                    items(count = lazyPagingItems.itemCount) { index ->
                        val project = lazyPagingItems[index]
                        if (project != null) {
                            StaggeredCard(index = index) {
                                ProjectCard(
                                    project = project,
                                    currentUserId = uiState.currentUserId,
                                    modifier = Modifier.clickable { goToProject(project) },
                                )
                            }
                        }
                    }

                    lazyPagingItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> item {
                                Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.primary,
                                        strokeWidth = 3.dp,
                                    )
                                }
                            }

                            loadState.append is LoadState.Loading -> item {
                                Box(Modifier.fillMaxWidth().padding(16.dp), Alignment.Center) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.primary,
                                        strokeWidth = 3.dp,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                            }

                            loadState.refresh is LoadState.Error -> item {
                                Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) {
                                    Text(
                                        "Ошибка загрузки",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // FAB
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(56.dp)
                    .cheapGlassEffect(
                        shape = CircleShape,
                        fillAlpha = 0.3f,
                        borderAlpha = 0.25f,
                        borderWidth = 0.5.dp,
                    )
                    .clickable { goToCreate() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Создать проект",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
}

// ─── Staggered entrance ─────────────────────────────────────────────────────

@Composable
private fun StaggeredCard(
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(index) {
        delay(index * 60L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) +
                slideInVertically(
                    animationSpec = spring(
                        dampingRatio = 0.7f,
                        stiffness = 300f,
                    ),
                    initialOffsetY = { it / 2 },
                ),
    ) {
        Box(modifier = modifier) {
            content()
        }
    }
}

// ─── Project card ───────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProjectCard(
    project: Project,
    currentUserId: String,
    modifier: Modifier = Modifier,
) {
    val isOwn = project.authorId == currentUserId

    Box(
        modifier = modifier
            .fillMaxWidth()
            .cheapGlassEffect(
                shape = RoundedCornerShape(16.dp),
                fillAlpha = 0.12f,
                borderAlpha = 0.15f,
                borderWidth = 0.5.dp,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Title + stage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(8.dp))
                StageChip(project.stage)
            }

            // Description
            if (project.description.isNotBlank()) {
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 2,
                )
            }

            // Roles
            if (project.neededRoles.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    project.neededRoles.take(3).forEach { role ->
                        Box(
                            modifier = Modifier
                                .cheapGlassEffect(
                                    shape = RoundedCornerShape(10.dp),
                                    fillAlpha = 0.1f,
                                    borderAlpha = 0.1f,
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                        ) {
                            Text(
                                text = role,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                    if (project.neededRoles.size > 3) {
                        Text(
                            "+${project.neededRoles.size - 3}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                }
            }

            // Footer: author + likes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (isOwn) "Мой проект" else project.authorName.orEmpty(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isOwn) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isOwn) FontWeight.SemiBold else FontWeight.Normal,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = project.likesCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

// ─── Stage chip ─────────────────────────────────────────────────────────────

@Composable
private fun StageChip(stage: ProjectStage) {
    val (label, color) = when (stage) {
        ProjectStage.IDEA -> "Идея" to MaterialTheme.colorScheme.tertiary
        ProjectStage.DEVELOPMENT -> "Разработка" to MaterialTheme.colorScheme.primary
        ProjectStage.TESTING -> "Тестирование" to MaterialTheme.colorScheme.secondary
        ProjectStage.COMPLETED -> "Завершён" to MaterialTheme.colorScheme.outline
        ProjectStage.UNKNOWN -> "Неизвестно" to MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = Modifier
            .cheapGlassEffect(
                shape = RoundedCornerShape(8.dp),
                fillAlpha = 0.05f,
                borderAlpha = 0.1f,
                tint = color,
            )
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium,
        )
    }
}

// ─── Background ─────────────────────────────────────────────────────────────

@Composable
private fun MovingCirclesBackground(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "projects_bg")

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
