package detailedProject

internal sealed interface DetailedProjectIntent {
    data object LikeProject : DetailedProjectIntent
    data class SetResponseMessage(val value: String) : DetailedProjectIntent
    data class SetCommentText(val value: String) : DetailedProjectIntent
    data object SubmitResponse : DetailedProjectIntent
    data object SubmitComment : DetailedProjectIntent
    data object ReloadComments : DetailedProjectIntent
    data class AcceptApplicant(val responseId: String) : DetailedProjectIntent
    data class RejectApplicant(val responseId: String) : DetailedProjectIntent
    data object DeleteProject : DetailedProjectIntent
    data object Refresh : DetailedProjectIntent
}
