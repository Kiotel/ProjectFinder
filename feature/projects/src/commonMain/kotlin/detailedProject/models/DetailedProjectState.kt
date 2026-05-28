package detailedProject.models

import androidx.compose.runtime.Stable
import models.Project
import models.ProjectApplicant
import models.ProjectComment

@Stable
internal data class DetailedProjectState(
    val isLiked: Boolean,
    val project: Project?,
    val isAuthor: Boolean,
    val comments: List<ProjectComment>,
    val applicants: List<ProjectApplicant>,
    val isLoadingApplicants: Boolean,
    val responseMessage: String,
    val commentText: String,
    val isLoadingComments: Boolean,
    val isSubmitting: Boolean,
) {
    internal constructor(internal: InternalDetailedProjectState) : this(
        isLiked = internal.isLiked,
        project = internal.project,
        isAuthor = internal.project != null && internal.currentUserId == internal.project.authorId,
        comments = internal.comments,
        applicants = internal.applicants,
        isLoadingApplicants = internal.isLoadingApplicants,
        responseMessage = internal.responseMessage,
        commentText = internal.commentText,
        isLoadingComments = internal.isLoadingComments,
        isSubmitting = internal.isSubmitting,
    )
}
