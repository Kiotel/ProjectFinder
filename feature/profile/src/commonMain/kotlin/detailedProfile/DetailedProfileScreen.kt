package detailedProfile

import ProfileViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.BrandTitle
import components.ScreenLayout
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi


@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun DetailedProfileScreen(
    modifier: Modifier = Modifier, vm: DetailedProfileViewModel, svm: ProfileViewModel
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    ScreenLayout { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding() + 36.dp).imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BrandTitle()
            Text("Экран профиля")
        }
    }
}