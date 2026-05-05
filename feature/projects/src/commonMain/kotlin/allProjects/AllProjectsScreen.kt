package allProjects

import allProjects.models.AllProjectsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import components.ScreenLayout
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlinx.coroutines.flow.Flow
import models.Project
import utils.SnackBarManager

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun AllProjectsScreen(
    modifier: Modifier = Modifier,
    pagingFlow: Flow<PagingData<Project>>,
    uiState: AllProjectsState,
    handleIntent: (intent: AllProjectsIntent) -> Unit,
    snackBarManager: SnackBarManager,
) {
    // 1. Collect the Flow into LazyPagingItems
    val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()

    ScreenLayout(
        modifier = modifier,
        snackBarManager = snackBarManager
    ) { innerPadding ->

        // 2. Use LazyColumn instead of rememberScrollState
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            stickyHeader {
                Text(text = lazyPagingItems.itemCount.toString())
            }
            item {
                Text(modifier = Modifier.padding(16.dp), text = "All projects:")
            }

            // 3. Populate the list
            items(count = lazyPagingItems.itemCount) { index ->
                val project = lazyPagingItems[index]
                if (project != null) {
                    // Replace this Text with your actual Project Card/Row Composable
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = "$index: Project: $project"
                    )
                }
            }

            // 4. Handle Loading & Error States (Pagination Indicator)
            lazyPagingItems.apply {
                when {
                    // Initial load state
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    // Loading next page state
                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    // Initial load error state
                    loadState.refresh is LoadState.Error -> {
                        val e = lazyPagingItems.loadState.refresh as LoadState.Error
                        item { ErrorItem(e.error.toString() ?: "Unknown error") }
                    }
                    // Loading next page error state
                    loadState.append is LoadState.Error -> {
                        val e = lazyPagingItems.loadState.append as LoadState.Error
                        item { ErrorItem(e.error.toString() ?: "Failed to load more") }
                    }
                }
            }
        }
    }
}

// Small helper composables for loading and errors
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
private fun ErrorItem(message: String) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = androidx.compose.ui.graphics.Color.Red)
    }
}