package mapppers

import models.Project
import models.ProjectStage
import models.ProjectStatus
import remote.apis.dtos.common.ResponseProjectDto
import kotlin.time.Instant

internal fun ResponseProjectDto.toDomain() = Project(
    id = this.id ?: "no id",
    authorId = this.authorId ?: "no author id",
    authorName = this.authorName ?: "no author name",
    title = this.title ?: "no title",
    description = this.description ?: "no description",
    briefDescription = this.briefDescription ?: "no brief description",
    stage = ProjectStage.fromString(this.stage),
    status = ProjectStatus.fromString(this.status),
    // Чтобы парсер думал, что это UTC надо добавить Z
    createdAt = Instant.parseOrNull(this.createdAt + "Z") ?: Instant.fromEpochSeconds(0),
    updatedAt = Instant.parseOrNull(this.updatedAt + "Z") ?: Instant.fromEpochSeconds(0),
    viewsCount = this.viewsCount ?: -1,
    likesCount = this.likesCount ?: -1,
    tags = this.tags ?: emptyList(),
    neededRoles = this.neededRoles?.map { it.title ?: "no title" } ?: emptyList() ,
)