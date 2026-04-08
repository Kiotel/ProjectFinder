package mapppers

import local.database.entities.ProjectEntity
import models.Project
import models.ProjectStage
import models.ProjectStatus
import remote.apis.dtos.common.ProjectDto
import kotlin.time.Clock.System
import kotlin.time.Instant

internal fun ProjectDto.toEntity() = ProjectEntity(
    id = this.id,
    authorId = this.authorId,
    title = this.title,
    description = this.description,
    briefDescription = this.briefDescription,
    stage = this.stage,
    status = this.status,
    viewsCount = this.viewsCount,
    likesCount = this.likesCount,
    tags = this.tags,
    neededRoles = this.neededRoles.map { it.title },
    authorName = this.authorName,
    // Чтобы парсер думал, что это UTC надо добавить Z
    createdAt = Instant.parseOrNull(this.createdAt + "Z")?.toEpochMilliseconds() ?: 0,
    updatedAt = Instant.parseOrNull(this.updatedAt + "Z")?.toEpochMilliseconds() ?: 0,
    lastFetched = System.now().toEpochMilliseconds()
)

internal fun ProjectDto.toDomain() = Project(
    id = this.id,
    authorId = this.authorId,
    title = this.title,
    description = this.description,
    briefDescription = this.briefDescription,
    stage = ProjectStage.fromString(this.stage),
    status = ProjectStatus.fromString(this.status),
    // Чтобы парсер думал, что это UTC надо добавить Z
    createdAt = Instant.parseOrNull(this.createdAt + "Z") ?: Instant.fromEpochSeconds(0),
    updatedAt = Instant.parseOrNull(this.updatedAt + "Z") ?: Instant.fromEpochSeconds(0),
    viewsCount = this.viewsCount,
    likesCount = this.likesCount,
    tags = this.tags,
    neededRoles = this.neededRoles.map { it.title },
    authorName = this.authorName,
)

internal fun ProjectEntity.toDomain() = Project(
    id = this.id,
    authorId = this.authorId,
    title = this.title,
    description = this.description,
    briefDescription = this.briefDescription,
    stage = ProjectStage.fromString(this.stage),
    status = ProjectStatus.fromString(this.status),
    createdAt = Instant.fromEpochSeconds(this.createdAt),
    updatedAt = Instant.fromEpochSeconds(this.updatedAt),
    viewsCount = this.viewsCount,
    likesCount = this.likesCount,
    tags = this.tags,
    neededRoles = this.neededRoles,
    authorName = this.authorId,
)