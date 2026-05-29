package detailedProject.models

import models.Project
import models.ProjectApplicant
import models.ProjectComment
import models.ProjectMember

internal data class InternalDetailedProjectState(
    val isLiked: Boolean = false,
    val project: Project? = null,
    val currentUserId: String = "",
    val comments: List<ProjectComment> = emptyList(),
    val applicants: List<ProjectApplicant> = emptyList(),
    val members: List<ProjectMember> = emptyList(),
    val isLoadingApplicants: Boolean = false,
    val isLoadingMembers: Boolean = false,
    val responseMessage: String = "",
    val commentText: String = "",
    val isLoadingComments: Boolean = false,
    val isSubmitting: Boolean = false,
    val isDeleted: Boolean = false,
)
