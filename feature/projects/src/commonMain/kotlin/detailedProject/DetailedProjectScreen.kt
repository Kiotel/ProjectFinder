package detailedProject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import components.ScreenLayout
import detailedProject.models.DetailedProjectState
import models.ProjectApplicant
import models.ProjectMember
import models.ProjectStage
import utils.SnackBarManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailedProjectScreen(
    modifier: Modifier = Modifier,
    uiState: DetailedProjectState,
    handleIntent: (intent: DetailedProjectIntent) -> Unit,
    snackBarManager: SnackBarManager,
    onEdit: (models.Project) -> Unit,
    onBack: () -> Unit,
) {
    val project = uiState.project ?: return

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onBack()
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить проект?") },
            text = { Text("Это действие нельзя отменить.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        handleIntent(DetailedProjectIntent.DeleteProject)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    ScreenLayout(
        modifier = modifier,
        snackBarManager = snackBarManager,
        topBar = {
            TopAppBar(
                title = { Text("Проект") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoadingComments || uiState.isLoadingApplicants || uiState.isLoadingMembers,
            onRefresh = { handleIntent(DetailedProjectIntent.Refresh) },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = project.authorName.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    StageChip(project.stage)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp),
                    ) {
                        IconButton(
                            onClick = { handleIntent(DetailedProjectIntent.LikeProject) },
                            modifier = Modifier.size(32.dp),
                        ) {
                            Icon(
                                if (uiState.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                tint = if (uiState.isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        Text(
                            text = project.likesCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            if (uiState.isAuthor) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onEdit(project) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Редактировать")
                    }
                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Удалить")
                    }
                }
            }

            HorizontalDivider()

            // Description
            if (project.description.isNotBlank()) {
                SectionCard(title = "О проекте") {
                    Text(project.description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Roles
            if (project.neededRoles.isNotEmpty()) {
                SectionCard(title = "Нужны в команду") {
                    RolesRow(project.neededRoles)
                }
            }

            // Tags
            if (project.tags.isNotEmpty()) {
                SectionCard(title = "Теги") {
                    RolesRow(project.tags)
                }
            }

            // Members
            MembersSection(
                members = uiState.members,
                isLoading = uiState.isLoadingMembers,
                onMemberClick = { /* Navigate to member profile */ },
            )

            // Applicants (author only) or Response form (others)
            if (uiState.isAuthor) {
                ApplicantsSection(
                    applicants = uiState.applicants,
                    isLoading = uiState.isLoadingApplicants,
                    onAccept = { handleIntent(DetailedProjectIntent.AcceptApplicant(it)) },
                    onReject = { handleIntent(DetailedProjectIntent.RejectApplicant(it)) },
                )
            } else {
                SectionCard(title = "Подать заявку") {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.responseMessage,
                        onValueChange = { handleIntent(DetailedProjectIntent.SetResponseMessage(it)) },
                        placeholder = { Text("Расскажите о себе и почему хотите участвовать") },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                    )
                    Button(
                        onClick = { handleIntent(DetailedProjectIntent.SubmitResponse) },
                        enabled = !uiState.isSubmitting && uiState.responseMessage.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    ) {
                        if (uiState.isSubmitting) CircularProgressIndicator(modifier = Modifier.size(18.dp))
                        else Text("Отправить заявку")
                    }
                }
            }

            // Comments
            SectionCard(title = "Комментарии") {
                if (uiState.isLoadingComments) {
                    Box(Modifier.fillMaxWidth(), Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                    }
                } else if (uiState.comments.isEmpty()) {
                    Text(
                        "Пока нет комментариев",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.comments.forEach { comment ->
                            val author = comment.authorName
                                ?: comment.authorId?.let { "Участник #$it" }
                                ?: "Участник"
                            Column {
                                Text(author, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                                Text(comment.content, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.commentText,
                    onValueChange = { handleIntent(DetailedProjectIntent.SetCommentText(it)) },
                    placeholder = { Text("Написать комментарий...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                )
                Button(
                    onClick = { handleIntent(DetailedProjectIntent.SubmitComment) },
                    enabled = !uiState.isSubmitting && uiState.commentText.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                ) {
                    Text("Отправить")
                }
            }
        }
    }
}
}

@Composable
private fun SectionCard(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RolesRow(items: List<String>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.forEach { item ->
            SuggestionChip(onClick = {}, label = { Text(item, style = MaterialTheme.typography.labelSmall) })
        }
    }
}

@Composable
private fun StageChip(stage: ProjectStage) {
    val (label, color) = when (stage) {
        ProjectStage.IDEA -> "Идея" to MaterialTheme.colorScheme.tertiary
        ProjectStage.DEVELOPMENT -> "Разработка" to MaterialTheme.colorScheme.primary
        ProjectStage.TESTING -> "Тестирование" to MaterialTheme.colorScheme.secondary
        ProjectStage.COMPLETED -> "Завершён" to MaterialTheme.colorScheme.outline
        ProjectStage.UNKNOWN -> "Неизвестно" to MaterialTheme.colorScheme.outline
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun ApplicantsSection(
    applicants: List<ProjectApplicant>,
    isLoading: Boolean,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit,
) {
    SectionCard(title = "Заявки (${applicants.size})") {
        if (isLoading) {
            Box(Modifier.fillMaxWidth(), Alignment.Center) { CircularProgressIndicator(modifier = Modifier.padding(8.dp)) }
        } else if (applicants.isEmpty()) {
            Text("Заявок пока нет", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                applicants.forEach { applicant ->
                    ApplicantItem(applicant = applicant, onAccept = onAccept, onReject = onReject)
                }
            }
        }
    }
}

@Composable
private fun ApplicantItem(
    applicant: ProjectApplicant,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                Text(applicant.username, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            }
            if (!applicant.message.isNullOrBlank()) {
                Text(applicant.message!!, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            val statusColor = when (applicant.status) {
                "принят" -> Color(0xFF2E7D32)
                "отклонён" -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            Text("Статус: ${applicant.status}", style = MaterialTheme.typography.labelSmall, color = statusColor)
            if (applicant.status == "рассматривается") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                    Button(onClick = { onAccept(applicant.responseId) }, modifier = Modifier.weight(1f)) {
                        Text("Принять")
                    }
                    OutlinedButton(
                        onClick = { onReject(applicant.responseId) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    ) {
                        Text("Отклонить")
                    }
                }
            }
        }
    }
}

@Composable
private fun MembersSection(
    members: List<ProjectMember>,
    isLoading: Boolean,
    onMemberClick: (String) -> Unit,
) {
    SectionCard(title = "Участники (${members.size})") {
        if (isLoading) {
            Box(Modifier.fillMaxWidth(), Alignment.Center) { CircularProgressIndicator(modifier = Modifier.padding(8.dp)) }
        } else if (members.isEmpty()) {
            Text("Нет участников", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                members.forEach { member ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = member.username,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                if (!member.firstName.isBlank()) {
                                    Text(
                                        text = member.firstName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
