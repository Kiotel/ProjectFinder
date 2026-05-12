package detailedProject.models

import models.Project
import models.ProjectStage
import models.ProjectStatus
import kotlin.time.Clock.System.now

internal data class InternalDetailedProjectState(
    val isLiked: Boolean = false,
    val project: Project = Project(
        id = "...",
        authorId = "...",
        title = "...",
        description = "...",
        briefDescription = "...",
        stage = ProjectStage.UNKNOWN,
        status = ProjectStatus.UNKNOWN,
        createdAt = now(),
        updatedAt = now(),
        viewsCount = -1,
        likesCount = -1,
        tags = emptyList(),
        neededRoles = emptyList(),
        authorName = "..."
    )
)
