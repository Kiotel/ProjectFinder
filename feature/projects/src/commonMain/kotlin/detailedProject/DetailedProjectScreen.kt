package detailedProject

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import components.ScreenLayout
import detailedProject.models.DetailedProjectState
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import utils.SnackBarManager

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun DetailedProjectScreen(
    modifier: Modifier = Modifier,
    uiState: DetailedProjectState,
    handleIntent: (intent: DetailedProjectIntent) -> Unit,
    snackBarManager: SnackBarManager,
) {
    ScreenLayout(
        modifier = modifier,
        snackBarManager = snackBarManager
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Id: ${uiState.project.id}")
            Text("Title: ${uiState.project.title}")
            Text("Brief description: ${uiState.project.briefDescription}")
            Text("Description: ${uiState.project.description}")
            Text("Author name: ${uiState.project.authorName}")
            Text("Author Id: ${uiState.project.authorId}")
            Text("Status: ${uiState.project.status}")
            Text("Stage: ${uiState.project.stage}")
            Text("Likes: ${uiState.project.likesCount}")
            Text("Views: ${uiState.project.viewsCount}")
            Text("Created at: ${uiState.project.createdAt}")
            Text("Updated at: ${uiState.project.updatedAt}")
        }
        // TODO: Сделать и протестить лайки и просмотры
    }
}
