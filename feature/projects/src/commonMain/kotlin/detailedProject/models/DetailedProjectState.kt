package detailedProject.models

import androidx.compose.runtime.Stable
import models.Project
import models.ProjectApplicant
import models.ProjectComment
import models.ProjectMember

@Stable
internal data class DetailedProjectState(
    val isLiked: Boolean,
    val project: Project?,
    val isAuthor: Boolean,
    val comments: List<ProjectComment>,
    val applicants: List<ProjectApplicant>,
    val members: List<ProjectMember>,
    val isLoadingApplicants: Boolean,
    val isLoadingMembers: Boolean,
    val responseMessage: String,
    val commentText: String,
    val isLoadingComments: Boolean,
    val isSubmitting: Boolean,
    val isDeleted: Boolean,
) {
    internal constructor(internal: InternalDetailedProjectState) : this(
        isLiked = internal.isLiked,
        project = internal.project,
        isAuthor = internal.project != null && internal.currentUserId == internal.project.authorId,
        comments = internal.comments,
        applicants = internal.applicants,
        members = internal.members,
        isLoadingApplicants = internal.isLoadingApplicants,
        isLoadingMembers = internal.isLoadingMembers,
        responseMessage = internal.responseMessage,
        commentText = internal.commentText,
        isLoadingComments = internal.isLoadingComments,
        isSubmitting = internal.isSubmitting,
        isDeleted = internal.isDeleted,
    )
}
