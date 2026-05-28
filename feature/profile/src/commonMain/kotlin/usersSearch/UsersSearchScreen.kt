package usersSearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.ScreenLayout
import models.UserProfile

@Composable
fun UsersSearchScreen(
    modifier: Modifier = Modifier,
    vm: UsersSearchViewModel,
    onUserClick: (UserProfile) -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    ScreenLayout(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
        ) {
            Text(
                text = "Поиск участников",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.query,
                onValueChange = vm::onQueryChange,
                placeholder = { Text("Имя, навык, вуз...") },
                singleLine = true,
            )
            Text(
                text = "Найдено: ${uiState.totalItems}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            val error = uiState.error
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.users, key = { it.id }) { user ->
                            UserCard(user = user, onClick = { onUserClick(user) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCard(user: UserProfile, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        ),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = user.displayName, style = MaterialTheme.typography.titleMedium)
            val meta = buildList {
                user.age?.let { add("$it лет") }
                user.university?.let { add(it) }
            }.joinToString(" · ")
            if (meta.isNotBlank()) {
                Text(text = meta, style = MaterialTheme.typography.bodySmall)
            }
            val skills = user.skills.take(3).joinToString(", ") { it.name }
            if (skills.isNotBlank()) {
                Text(text = skills, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
