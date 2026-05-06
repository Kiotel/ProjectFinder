package allProjects

import allProjects.models.AllProjectsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import components.ScreenLayout
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlinx.coroutines.flow.Flow
import models.Project
import models.ProjectStage
import models.ProjectStatus
import theme.AppTheme
import utils.SnackBarManager
import kotlin.time.Clock.System.now
import kotlin.time.Instant

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun AllProjectsScreen(
    modifier: Modifier = Modifier,
    pagingFlow: Flow<PagingData<Project>>,
    uiState: AllProjectsState,
    handleIntent: (intent: AllProjectsIntent) -> Unit,
    snackBarManager: SnackBarManager,
    goToProject: (project: Project) -> Unit
) {
    val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()

    val counterText by remember {
        derivedStateOf {
            "${lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index?.plus(1) ?: 0}/${lazyPagingItems.itemCount}"
        }
    }

    ScreenLayout(
        modifier = modifier,
        snackBarManager = snackBarManager
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = 16.dp
            ),
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = lazyPagingItems.itemCount) { index ->
                val project = lazyPagingItems[index]
                if (project != null) {
                    ProjectCard(modifier = Modifier.clickable {
                        goToProject(project)
                    }, project = project)
                }
            }

            lazyPagingItems.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingItem() }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val e = lazyPagingItems.loadState.refresh as LoadState.Error
                        item { ErrorItem(e.error.toString()) }
                    }

                    loadState.append is LoadState.Error -> {
                        val e = lazyPagingItems.loadState.append as LoadState.Error
                        item { ErrorItem(e.error.toString()) }
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                text = counterText
            )
        }
    }
}

@Composable
private fun LoadingItem() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ProjectCard(modifier: Modifier = Modifier, project: Project) {
    Card(modifier = modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(project.title)
            Text(project.briefDescription)
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 420,
    heightDp = 720
)
@Composable
private fun ProjectCardPreview() {
    AppTheme {
        val project = Project(
            id = "previewId",
            authorId = "PreviewAuthorId",
            title = "Preview title",
            description = "Preview description",
            briefDescription = "Preview brief description",
            stage = ProjectStage.IDEA,
            status = ProjectStatus.ACTIVE,
            createdAt = now(),
            updatedAt = Instant.fromEpochMilliseconds(now().toEpochMilliseconds() - 365 * 24 * 60 * 60 * 1000),
            viewsCount = 1234,
            likesCount = 4321,
            tags = listOf("tag1", "tag2", "tag3"),
            neededRoles = listOf("role1", "role2", "role3"),
            authorName = "Preview author name"
        )

        ProjectCard(project = project)
    }
}


@Composable
private fun ErrorItem(message: String) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.Red)
    }
}