package createProject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.ScreenLayout
import createProject.models.CreateProjectState
import utils.SnackBarManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateProjectScreen(
    uiState: CreateProjectState,
    handleIntent: (CreateProjectIntent) -> Unit,
    snackBarManager: SnackBarManager,
    onBack: () -> Unit,
) {
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBack()
            handleIntent(CreateProjectIntent.ResetSuccess)
        }
    }

    ScreenLayout(
        modifier = Modifier.fillMaxSize(),
        snackBarManager = snackBarManager
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Создать проект") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { handleIntent(CreateProjectIntent.SetTitle(it)) },
                    label = { Text("Название проекта*") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.industry,
                    onValueChange = { handleIntent(CreateProjectIntent.SetIndustry(it)) },
                    label = { Text("Сфера (IT, Дизайн, и т.д.)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { handleIntent(CreateProjectIntent.SetDescription(it)) },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                HorizontalDivider()

                Text("Нужные роли", style = MaterialTheme.typography.titleMedium)

                uiState.roles.forEachIndexed { index, role ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(role.first, modifier = Modifier.weight(1f))
                        Text("${role.second} мест", style = MaterialTheme.typography.bodySmall)
                        IconButton(onClick = { handleIntent(CreateProjectIntent.RemoveRole(index)) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить роль", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }

                var newRoleName by remember { mutableStateOf("") }
                var newRoleSpots by remember { mutableStateOf("1") }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newRoleName,
                        onValueChange = { newRoleName = it },
                        label = { Text("Роль") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newRoleSpots,
                        onValueChange = { if (it.all { char -> char.isDigit() }) newRoleSpots = it },
                        label = { Text("Мест") },
                        modifier = Modifier.width(70.dp),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (newRoleName.isNotBlank()) {
                                handleIntent(CreateProjectIntent.AddRole(newRoleName, newRoleSpots.toIntOrNull() ?: 1))
                                newRoleName = ""
                                newRoleSpots = "1"
                            }
                        },
                        enabled = newRoleName.isNotBlank()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить")
                    }
                }

                Button(
                    onClick = { handleIntent(CreateProjectIntent.Submit) },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 32.dp),
                    enabled = !uiState.isLoading && uiState.title.isNotBlank()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Опубликовать проект")
                    }
                }
            }
        }
    }
}
