package createProject

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
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
        snackBarManager = snackBarManager,
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.projectId != null) "Редактировать проект" else "Создать проект") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
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
                RoleCard(
                    index = index,
                    role = role,
                    onRemove = { handleIntent(CreateProjectIntent.RemoveRole(index)) },
                    onAddSkill = { skill -> handleIntent(CreateProjectIntent.AddSkillToRole(index, skill)) },
                    onRemoveSkill = { skill -> handleIntent(CreateProjectIntent.RemoveSkillFromRole(index, skill)) },
                )
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
                    Text(if (uiState.projectId != null) "Сохранить изменения" else "Опубликовать проект")
                }
            }
        }
    }
}

// ─── Role Card ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RoleCard(
    index: Int,
    role: models.ProjectRole,
    onRemove: () -> Unit,
    onAddSkill: (String) -> Unit,
    onRemoveSkill: (String) -> Unit,
) {
    var showSkills by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = role.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "${role.spots} мест",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (role.skills.isNotEmpty()) {
                Text(
                    text = "${role.skills.size} навык(ов)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить роль", tint = MaterialTheme.colorScheme.error)
            }
        }

        // Skill chips
        if (role.skills.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                role.skills.forEach { skill ->
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(16.dp),
                            )
                            .clickable { onRemoveSkill(skill) }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = skill,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Удалить",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    }
                }
            }
        }

        if (!showSkills) {
            Button(
                onClick = { showSkills = true },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Навыки", style = MaterialTheme.typography.labelMedium)
            }
        }

        if (showSkills) {
            RoleSkillsPicker(
                selectedSkills = role.skills,
                onAddSkill = onAddSkill,
                onRemoveSkill = onRemoveSkill,
                onDismiss = { showSkills = false },
            )
        }
    }
}

// ─── Role Skills Picker ─────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RoleSkillsPicker(
    selectedSkills: List<String>,
    onAddSkill: (String) -> Unit,
    onRemoveSkill: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val availableSkills = listOf(
        "Kotlin", "Android", "iOS", "Swift", "Java",
        "Python", "Go", "JavaScript", "TypeScript",
        "React", "Vue", "SQL", "Figma",
        "UI/UX Design", "QA Engineering"
    ).sorted()

    val filteredSkills = remember(searchQuery, availableSkills, selectedSkills) {
        availableSkills
            .filter { it !in selectedSkills }
            .filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Требуемые навыки",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Закрыть", modifier = Modifier.size(18.dp))
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    expanded = true
                },
                modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                    if (focusState.isFocused) expanded = true
                },
                placeholder = { Text("Поиск навыков...", style = MaterialTheme.typography.bodySmall) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = ""; expanded = false }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                textStyle = MaterialTheme.typography.bodySmall,
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                ),
            )

            DropdownMenu(
                expanded = expanded && filteredSkills.isNotEmpty(),
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.8f),
                properties = PopupProperties(focusable = false),
            ) {
                filteredSkills.forEach { skill ->
                    DropdownMenuItem(
                        text = { Text(skill, style = MaterialTheme.typography.bodySmall) },
                        onClick = {
                            onAddSkill(skill)
                            searchQuery = ""
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
