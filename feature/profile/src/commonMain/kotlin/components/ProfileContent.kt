package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import modifiers.cheapGlassEffect
import models.Skill
import models.UserProfile

@Composable
internal fun ProfileLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Загружаем профиль...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
internal fun ProfileError(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
internal fun ProfileHeader(profile: UserProfile, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .cheapGlassEffect(
                shape = RoundedCornerShape(20.dp),
                fillAlpha = 0.12f,
                borderAlpha = 0.2f,
                borderWidth = 1.dp,
            )
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar with initials
            AvatarCircle(
                name = profile.displayName,
                modifier = Modifier.size(64.dp),
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                val subtitle = buildList {
                    profile.age?.let { add("$it лет") }
                    profile.university?.let { add(it) }
                    profile.city?.let { add(it) }
                }.joinToString(" · ")
                if (subtitle.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                profile.email.takeIf { it.isNotBlank() }?.let { email ->
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

@Composable
private fun AvatarCircle(
    name: String,
    modifier: Modifier = Modifier,
) {
    val initials = name.split(" ").take(2).joinToString("") { it.firstOrNull()?.uppercase() ?: "" }
        .ifEmpty { "?" }

    val bgColor = MaterialTheme.colorScheme.primaryContainer
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
internal fun ProfileSections(profile: UserProfile, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        var visibleSections by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visibleSections = true
        }

        // Collect all sections
        val sections = buildList {
            add(
                SectionData(
                    title = "Образование",
                    icon = Icons.Default.School,
                    content = buildList {
                        profile.university?.let { add(it) }
                        profile.faculty?.let { add(it) }
                        val program = listOfNotNull(profile.programCode, profile.studyMode)
                            .joinToString(" · ")
                        if (program.isNotBlank()) add(program)
                        profile.city?.let { add(it) }
                    },
                )
            )
            if (profile.skills.isNotEmpty() || profile.interests.isNotEmpty()) {
                add(
                    SectionData(
                        title = "Навыки",
                        icon = Icons.Default.Star,
                        content = emptyList(),
                        skills = profile.skills,
                        interests = profile.interests.takeIf { it.isNotEmpty() },
                    )
                )
            }
            if (!profile.schedule.isNullOrBlank() || !profile.goals.isNullOrBlank()) {
                add(
                    SectionData(
                        title = "Доступность",
                        icon = Icons.Default.WorkHistory,
                        content = buildList {
                            profile.schedule?.let { add("График: $it") }
                            profile.goals?.let { add("Цели: $it") }
                        },
                    )
                )
            }
            if (profile.qualities.isNotEmpty()) {
                add(
                    SectionData(
                        title = "Качества",
                        icon = Icons.Default.CheckCircle,
                        content = listOf(profile.qualities.joinToString(", ")),
                    )
                )
            }
            profile.goals?.takeIf { it.isNotBlank() }?.let { about ->
                add(
                    SectionData(
                        title = "О себе",
                        icon = Icons.Default.CheckCircle,
                        content = listOf(about),
                    )
                )
            }
            if (profile.contacts.isNotEmpty()) {
                add(
                    SectionData(
                        title = "Контакты",
                        icon = Icons.Default.Phone,
                        content = profile.contacts.map { "${it.type}: ${it.value}" },
                    )
                )
            }
            profile.portfolioUrl?.let { url ->
                add(
                    SectionData(
                        title = "Портфолио",
                        icon = Icons.Default.WorkHistory,
                        content = listOf(url),
                    )
                )
            }
        }

        sections.forEachIndexed { index, section ->
            StaggeredSection(
                visible = visibleSections,
                index = index,
                section = section,
            )
        }
    }
}

private data class SectionData(
    val title: String,
    val icon: ImageVector,
    val content: List<String> = emptyList(),
    val skills: List<Skill> = emptyList(),
    val interests: List<String>? = null,
)

@Composable
private fun StaggeredSection(
    visible: Boolean,
    index: Int,
    section: SectionData,
) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (visible) {
            delay(index * 60L)
            show = true
        }
    }

    AnimatedVisibility(
        visible = show,
        enter = fadeIn(animationSpec = spring(dampingRatio = 0.8f, stiffness = 200f)) +
                slideInVertically(
                    animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
                    initialOffsetY = { it / 3 },
                ),
    ) {
        ProfileSectionCard(
            title = section.title,
            icon = section.icon,
        ) {
            if (section.skills.isNotEmpty()) {
                SkillChips(section.skills)
            }
            section.interests?.let { interests ->
                Spacer(Modifier.height(8.dp))
                ProfileLine("Интересы: ${interests.joinToString(", ")}")
            }
            section.content.forEach { line ->
                ProfileLine(line)
            }
        }
    }
}

@Composable
private fun ProfileSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .cheapGlassEffect(
                shape = RoundedCornerShape(16.dp),
                fillAlpha = 0.1f,
                borderAlpha = 0.15f,
                borderWidth = 0.5.dp,
            ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(8.dp))
            Column(content = content)
        }
    }
}

@Composable
private fun ProfileLine(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 3.dp),
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillChips(skills: List<Skill>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        skills.forEach { skill ->
            Box(
                modifier = Modifier
                    .cheapGlassEffect(
                        shape = RoundedCornerShape(10.dp),
                        fillAlpha = 0.15f,
                        borderAlpha = 0.1f,
                        borderWidth = 0.5.dp,
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp),
            ) {
                Text(
                    text = skill.level?.let { "${skill.name} ($it)" } ?: skill.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}
