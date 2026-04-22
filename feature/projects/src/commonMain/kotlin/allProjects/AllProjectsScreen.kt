package allProjects

import allProjects.models.AllProjectsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import components.ScreenLayout
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import utils.SnackBarManager

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun AllProjectsScreen(
    modifier: Modifier = Modifier,
    uiState: AllProjectsState,
    handleIntent: (intent: AllProjectsIntent) -> Unit,
    snackBarManager: SnackBarManager,
) {
    val scrollState = rememberScrollState()
    ScreenLayout(
        modifier = modifier,
        snackBarManager = snackBarManager
    ) { innerPadding ->
        Text(modifier = Modifier.padding(innerPadding), text = "All projects:")
    }
}
