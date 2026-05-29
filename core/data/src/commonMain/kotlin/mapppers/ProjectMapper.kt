package mapppers

import kotlinx.serialization.json.Json
import models.Project
import models.ProjectRole
import models.ProjectStage
import models.ProjectStatus
import remote.apis.dtos.common.ProjectRoleDto
import remote.apis.dtos.common.ResponseProjectDto
import kotlin.time.Instant

private val json = Json { ignoreUnknownKeys = true }

internal fun ResponseProjectDto.toDomain() = Project(
    id = id?.toString() ?: "0",
    authorId = authorId?.toString() ?: "0",
    authorName = authorName.orEmpty(),
    title = title.orEmpty(),
    description = description.orEmpty(),
    briefDescription = briefDescription ?: description.orEmpty(),
    stage = ProjectStage.fromString(stage ?: status),
    status = ProjectStatus.fromString(status),
    createdAt = parseInstant(createdAt),
    updatedAt = parseInstant(updatedAt ?: createdAt),
    viewsCount = viewsCount ?: 0,
    likesCount = likesCount ?: 0,
    tags = tags.orEmpty(),
    neededRoles = roleNames(),
    roles = rolesList(),
    industry = industry,
)

private fun ResponseProjectDto.roleNames(): List<String> {
    val roles = roles ?: neededRoles ?: return emptyList()
    return roles.mapNotNull { it.roleName ?: it.title }.filter { it.isNotBlank() }
}

private fun ResponseProjectDto.rolesList(): List<ProjectRole> {
    val roles = roles ?: neededRoles ?: return emptyList()
    return roles.map { dto ->
        ProjectRole(
            name = dto.roleName ?: dto.title ?: "Role",
            spots = dto.spotsTotal ?: 1,
            skills = dto.parseSkills()
        )
    }
}

// if we need more detailed role info in the future, we can use this
private fun ProjectRoleDto.parseSkills(): List<String> {
    val skills = requiredSkills ?: return emptyList()
    return try {
        json.decodeFromString<List<String>>(skills)
    } catch (e: Exception) {
        if (skills.isNotBlank() && skills != "[]") listOf(skills) else emptyList()
    }
}

private fun parseInstant(value: String?): Instant {
    if (value.isNullOrBlank()) return Instant.fromEpochSeconds(0)
    val normalized = if (value.endsWith('Z')) value else "${value}Z"
    return Instant.parseOrNull(normalized) ?: Instant.fromEpochSeconds(0)
}
