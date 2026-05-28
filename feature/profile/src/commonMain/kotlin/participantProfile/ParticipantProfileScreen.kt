package participantProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.ProfileError
import components.ProfileHeader
import components.ProfileLoading
import components.ProfileSections
import components.ScreenLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantProfileScreen(
    userId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    vm: ParticipantProfileViewModel,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(userId) { vm.load(userId) }

    ScreenLayout(modifier = modifier, snackBarManager = vm.snackBarManager) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TopAppBar(
                title = { Text("Профиль участника") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
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
                        ProfileSections(profile)
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { vm.bookmark() },
                enabled = uiState.profile != null,
            ) {
                Icon(Icons.Default.Bookmark, contentDescription = null)
                Text("В избранное", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
