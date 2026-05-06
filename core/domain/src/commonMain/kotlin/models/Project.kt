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
    val authorName: String,
)

enum class ProjectStage {
    IDEA,
    DEVELOPMENT,
    TESTING,
    COMPLETED,
    UNKNOWN;

    companion object {
        fun fromString(value: String): ProjectStage {
            return ProjectStage.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}

enum class ProjectStatus {
    ACTIVE,
    ARCHIVED,
    BLOCKED,
    UNKNOWN;

    companion object {
        fun fromString(value: String): ProjectStatus {
            return ProjectStatus.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}