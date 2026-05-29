package models

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Stable
@Serializable
data class Project(
    val id: String,
    val authorId: String,
    val title: String,
    val description: String,
    val briefDescription: String,
    val stage: ProjectStage,
    val status: ProjectStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val viewsCount: Int,
    val likesCount: Int,
    val tags: List<String>,
    val neededRoles: List<String>,
    val roles: List<ProjectRole> = emptyList(),
    val authorName: String,
    val industry: String? = null,
)

enum class ProjectStage {
    IDEA,
    DEVELOPMENT,
    TESTING,
    COMPLETED,
    UNKNOWN;

    companion object {
        fun fromString(value: String?): ProjectStage {
            val normalized = value?.trim()?.lowercase() ?: return UNKNOWN
            return when (normalized) {
                "idea" -> IDEA
                "development", "dev" -> DEVELOPMENT
                "testing", "test" -> TESTING
                "completed", "done" -> COMPLETED
                else -> entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
            }
        }
    }
}

enum class ProjectStatus {
    ACTIVE,
    ARCHIVED,
    BLOCKED,
    UNKNOWN;

    companion object {
        fun fromString(value: String?): ProjectStatus {
            val normalized = value?.trim()?.lowercase() ?: return UNKNOWN
            return when (normalized) {
                "active", "idea" -> ACTIVE
                "archived" -> ARCHIVED
                "blocked" -> BLOCKED
                else -> entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
            }
        }
    }
}