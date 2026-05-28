package detailedProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.ProfileError
import components.ProfileHeader
import components.ProfileLoading
import components.ProfileSections
import components.ScreenLayout

@Composable
internal fun DetailedProfileScreen(
    modifier: Modifier = Modifier,
    vm: DetailedProfileViewModel,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ScreenLayout(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding),
        ) {
            Text(
                text = "Мой профиль",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            val error = uiState.error
            val profile = uiState.profile
            when {
                uiState.isLoading -> ProfileLoading()
                error != null -> ProfileError(error)
                profile != null -> {
                    ProfileHeader(profile)
                    ProfileSections(profile)

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    ) {
                        Text("Выйти из аккаунта")
                    }

                    Button(
                        onClick = { vm.handleIntent(DetailedProfileIntent.DeleteAccount, onDeleteAccount) },
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("Удалить аккаунт")
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
