package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import models.Skill
import models.UserProfile

@Composable
internal fun ProfileLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ProfileError(message: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(16.dp),
        text = message,
        color = MaterialTheme.colorScheme.error,
    )
}

@Composable
internal fun ProfileHeader(profile: UserProfile, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = profile.displayName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        val subtitle = buildList {
            profile.age?.let { add("$it лет") }
            profile.university?.let { add(it) }
            profile.city?.let { add(it) }
        }.joinToString(" · ")
        if (subtitle.isNotBlank()) {
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
        profile.email.takeIf { it.isNotBlank() }?.let {
            Text(text = it, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
internal fun ProfileSections(profile: UserProfile, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ProfileSectionCard(title = "Образование") {
            profile.university?.let { ProfileLine(it) }
            profile.faculty?.let { ProfileLine(it) }
            val program = listOfNotNull(profile.programCode, profile.studyMode).joinToString(" · ")
            if (program.isNotBlank()) ProfileLine(program)
            profile.city?.let { ProfileLine(it) }
        }
        if (profile.skills.isNotEmpty() || profile.interests.isNotEmpty()) {
            ProfileSectionCard(title = "Навыки") {
                SkillChips(profile.skills)
                if (profile.interests.isNotEmpty()) {
                    ProfileLine("Интересы: ${profile.interests.joinToString(", ")}")
                }
            }
        }
        if (!profile.schedule.isNullOrBlank() || !profile.goals.isNullOrBlank()) {
            ProfileSectionCard(title = "Доступность") {
                profile.schedule?.let { ProfileLine("График: $it") }
                profile.goals?.let { ProfileLine("Цели: $it") }
            }
        }
        if (profile.qualities.isNotEmpty()) {
            ProfileSectionCard(title = "Качества") {
                ProfileLine(profile.qualities.joinToString(", "))
            }
        }
        profile.goals?.takeIf { it.isNotBlank() }?.let { about ->
            ProfileSectionCard(title = "О себе") {
                ProfileLine(about)
            }
        }
        if (profile.contacts.isNotEmpty()) {
            ProfileSectionCard(title = "Контакты") {
                profile.contacts.forEach { contact ->
                    ProfileLine("${contact.type}: ${contact.value}")
                }
            }
        }
        profile.portfolioUrl?.let { url ->
            ProfileSectionCard(title = "Портфолио") {
                ProfileLine(url)
            }
        }
    }
}

@Composable
private fun ProfileSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        ),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Column(modifier = Modifier.padding(top = 8.dp), content = content)
        }
    }
}

@Composable
private fun ProfileLine(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 4.dp),
        text = text,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillChips(skills: List<Skill>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        skills.forEach { skill ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    text = skill.level?.let { "${skill.name} ($it)" } ?: skill.name,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}
