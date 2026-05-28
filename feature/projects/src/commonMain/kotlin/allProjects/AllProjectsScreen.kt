package allProjects

import allProjects.models.AllProjectsState
import allProjects.models.ProjectFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import components.ScreenLayout
import kotlinx.coroutines.flow.Flow
import models.Project
import models.ProjectStage
import utils.SnackBarManager

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

    ScreenLayout(modifier = modifier, snackBarManager = snackBarManager) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Search bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { handleIntent(AllProjectsIntent.UpdateSearchQuery(it)) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Поиск проектов...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    ),
                )

                // Filter tabs
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProjectFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = uiState.filter == filter,
                            onClick = { handleIntent(AllProjectsIntent.SetFilter(filter)) },
                            label = {
                                Text(
                                    when (filter) {
                                        ProjectFilter.ALL -> "Все"
                                        ProjectFilter.MINE -> "Мои"
                                        ProjectFilter.JOINED -> "Участвую"
                                    }
                                )
                            },
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp
                    ),
                ) {
                    val filtered = (0 until lazyPagingItems.itemCount)
                        .mapNotNull { lazyPagingItems.peek(it) }
                        .let { all ->
                            when (uiState.filter) {
                                ProjectFilter.ALL -> null // show all via paging
                                ProjectFilter.MINE -> all.filter { it.authorId == uiState.currentUserId }
                                ProjectFilter.JOINED -> all.filter {
                                    it.authorId != uiState.currentUserId
                                }
                            }
                        }

                    if (filtered != null) {
                        items(filtered.size) { index ->
                            ProjectCard(
                                project = filtered[index],
                                currentUserId = uiState.currentUserId,
                                modifier = Modifier.clickable { goToProject(filtered[index]) },
                            )
                        }
                    } else {
                        items(lazyPagingItems.itemCount) { index ->
                            val project = lazyPagingItems[index]
                            if (project != null) {
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
                                    CircularProgressIndicator()
                                }
                            }
                            loadState.append is LoadState.Loading -> item {
                                Box(Modifier.fillMaxWidth().padding(16.dp), Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }
                            loadState.refresh is LoadState.Error -> item {
                                Text(
                                    "Ошибка загрузки",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(16.dp),
                                )
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = goToCreate,
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать проект")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProjectCard(
    project: Project,
    currentUserId: String,
    modifier: Modifier = Modifier,
) {
    val isOwn = project.authorId == currentUserId
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                StageChip(project.stage)
            }

            if (project.description.isNotBlank()) {
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                )
            }

            if (project.neededRoles.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    project.neededRoles.take(3).forEach { role ->
                        SuggestionChip(onClick = {}, label = { Text(role, style = MaterialTheme.typography.labelSmall) })
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(0.dp).let { it },
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

@Composable
private fun StageChip(stage: ProjectStage) {
    val (label, color) = when (stage) {
        ProjectStage.IDEA -> "Идея" to MaterialTheme.colorScheme.tertiary
        ProjectStage.DEVELOPMENT -> "Разработка" to MaterialTheme.colorScheme.primary
        ProjectStage.TESTING -> "Тестирование" to MaterialTheme.colorScheme.secondary
        ProjectStage.COMPLETED -> "Завершён" to MaterialTheme.colorScheme.outline
        ProjectStage.UNKNOWN -> "Неизвестно" to MaterialTheme.colorScheme.outline
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium,
        )
    }
}
