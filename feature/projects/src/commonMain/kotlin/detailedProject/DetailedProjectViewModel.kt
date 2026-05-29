package detailedProject

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import detailedProject.models.DetailedProjectState
import detailedProject.models.InternalDetailedProjectState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.Project
import useCases.DeleteProjectUseCase
import useCases.GetApplicantsUseCase
import useCases.GetCommentsUseCase
import useCases.GetProjectMembersUseCase
import useCases.GetUserProfileUseCase
import useCases.LikeProjectUseCase
import useCases.PostCommentUseCase
import useCases.PostProjectResponseUseCase
import useCases.UpdateApplicantStatusUseCase
import utils.SnackBarManager

internal class DetailedProjectViewModel(
    private val likeProjectUseCase: LikeProjectUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val postCommentUseCase: PostCommentUseCase,
    private val postProjectResponseUseCase: PostProjectResponseUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getApplicantsUseCase: GetApplicantsUseCase,
    private val updateApplicantStatusUseCase: UpdateApplicantStatusUseCase,
    private val getProjectMembersUseCase: GetProjectMembersUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    val snackBarManager: SnackBarManager,
) : ViewModel() {

    private val _internalState = MutableStateFlow(InternalDetailedProjectState())

    val uiState: StateFlow<DetailedProjectState> = _internalState.map { DetailedProjectState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailedProjectState(InternalDetailedProjectState()),
        )

    init {
        viewModelScope.launch {
            val userId = getUserProfileUseCase.current().first().getOrNull()?.id ?: ""
            updateState { it.copy(currentUserId = userId) }
        }
    }

    fun setProject(project: Project) {
        updateState { it.copy(project = project) }
        loadComments(project.id)
        loadMembers(project.id)
        // Load applicants only if current user is the author
        viewModelScope.launch {
            val userId = _internalState.value.currentUserId
            if (userId == project.authorId) loadApplicants(project.id)
        }
    }

    private fun loadComments(projectId: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoadingComments = true) }
            getCommentsUseCase(projectId).fold(
                onSuccess = { comments -> updateState { it.copy(isLoadingComments = false, comments = comments) } },
                onFailure = { updateState { it.copy(isLoadingComments = false) } },
            )
        }
    }

    private fun loadApplicants(projectId: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoadingApplicants = true) }
            getApplicantsUseCase(projectId).fold(
                onSuccess = { list -> updateState { it.copy(isLoadingApplicants = false, applicants = list) } },
                onFailure = { updateState { it.copy(isLoadingApplicants = false) } },
            )
        }
    }

    private fun loadMembers(projectId: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoadingMembers = true) }
            getProjectMembersUseCase(projectId).fold(
                onSuccess = { members -> updateState { it.copy(isLoadingMembers = false, members = members) } },
                onFailure = { updateState { it.copy(isLoadingMembers = false) } },
            )
        }
    }

    private fun updateApplicantStatus(responseId: String, status: String) {
        val projectId = _internalState.value.project?.id ?: return
        viewModelScope.launch {
            updateApplicantStatusUseCase(responseId, status).fold(
                onSuccess = {
                    loadApplicants(projectId)
                    loadMembers(projectId)
                },
                onFailure = { snackBarManager.showMessage("Ошибка: ${it.message}", SnackbarDuration.Short) },
            )
        }
    }

    private fun likeProject() {
        val projectId = uiState.value.project?.id ?: return
        viewModelScope.launch {
            likeProjectUseCase(projectId).collect { result ->
                result.fold(
                    onSuccess = { isLiked -> updateState { it.copy(isLiked = isLiked) } },
                    onFailure = { snackBarManager.showMessage("Ошибка лайка", SnackbarDuration.Short) },
                )
            }
        }
    }

    private fun submitResponse() {
        val project = uiState.value.project ?: return
        val message = uiState.value.responseMessage.trim()
        if (message.isBlank()) return
        viewModelScope.launch {
            updateState { it.copy(isSubmitting = true) }
            postProjectResponseUseCase(project.id, message).fold(
                onSuccess = {
                    updateState { it.copy(isSubmitting = false, responseMessage = "") }
                    snackBarManager.showMessage("Отклик отправлен", SnackbarDuration.Short)
                },
                onFailure = {
                    updateState { it.copy(isSubmitting = false) }
                    snackBarManager.showMessage(it.message ?: "Ошибка отклика", SnackbarDuration.Short)
                },
            )
        }
    }

    private fun submitComment() {
        val project = uiState.value.project ?: return
        val text = uiState.value.commentText.trim()
        if (text.isBlank()) return
        viewModelScope.launch {
            updateState { it.copy(isSubmitting = true) }
            postCommentUseCase(project.id, text).fold(
                onSuccess = {
                    updateState { it.copy(isSubmitting = false, commentText = "") }
                    loadComments(project.id)
                },
                onFailure = {
                    updateState { it.copy(isSubmitting = false) }
                    snackBarManager.showMessage(it.message ?: "Ошибка комментария", SnackbarDuration.Short)
                },
            )
        }
    }

    private fun deleteProject() {
        val projectId = uiState.value.project?.id ?: return
        viewModelScope.launch {
            updateState { it.copy(isSubmitting = true) }
            deleteProjectUseCase(projectId).fold(
                onSuccess = {
                    updateState { it.copy(isSubmitting = false, isDeleted = true) }
                    snackBarManager.showMessage("Проект удалён")
                },
                onFailure = {
                    updateState { it.copy(isSubmitting = false) }
                    snackBarManager.showMessage("Ошибка при удалении: ${it.message}")
                }
            )
        }
    }

    private fun updateState(mutation: (InternalDetailedProjectState) -> InternalDetailedProjectState) {
        _internalState.update(mutation)
    }

    fun handleIntent(intent: DetailedProjectIntent) {
        when (intent) {
            DetailedProjectIntent.LikeProject -> likeProject()
            is DetailedProjectIntent.SetResponseMessage -> updateState { it.copy(responseMessage = intent.value) }
            is DetailedProjectIntent.SetCommentText -> updateState { it.copy(commentText = intent.value) }
            DetailedProjectIntent.SubmitResponse -> submitResponse()
            DetailedProjectIntent.SubmitComment -> submitComment()
            DetailedProjectIntent.ReloadComments -> uiState.value.project?.id?.let { loadComments(it) }
            is DetailedProjectIntent.AcceptApplicant -> updateApplicantStatus(intent.responseId, "принято")
            is DetailedProjectIntent.RejectApplicant -> updateApplicantStatus(intent.responseId, "отклонён")
            DetailedProjectIntent.DeleteProject -> deleteProject()
            DetailedProjectIntent.Refresh -> uiState.value.project?.let { setProject(it) }
        }
    }
}
