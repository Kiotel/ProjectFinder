package notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.ScreenLayout

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    vm: NotificationsViewModel,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val scroll = rememberScrollState()

    ScreenLayout(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Уведомления",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 12.dp),
            )
            Button(onClick = vm::refresh, modifier = Modifier.padding(bottom = 8.dp)) {
                Text("Обновить")
            }
            val error = uiState.error
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                else -> {
                    Text(
                        modifier = Modifier.verticalScroll(scroll),
                        text = uiState.content.ifBlank { "Нет уведомлений" },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
