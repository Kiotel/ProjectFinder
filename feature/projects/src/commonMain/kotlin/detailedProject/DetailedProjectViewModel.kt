package detailedProject

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import detailedProject.models.DetailedProjectState
import detailedProject.models.InternalDetailedProjectState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.Project
import useCases.LikeProjectUseCase
import utils.SnackBarManager

internal class DetailedProjectViewModel(
    private val likeProjectUseCase: LikeProjectUseCase,
    val snackBarManager: SnackBarManager
) : ViewModel() {

    fun setProject(project: Project) {
        updateState {
            it.copy(
                project = project
            )
        }
    }

    private val _internalState = MutableStateFlow(
        InternalDetailedProjectState()
    )

    val uiState: StateFlow<DetailedProjectState> = _internalState.map { internalState ->
        DetailedProjectState(
            isLiked = internalState.isLiked,
            project = internalState.project
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailedProjectState(InternalDetailedProjectState())
    )

    private fun updateState(mutation: (InternalDetailedProjectState) -> InternalDetailedProjectState) {
        _internalState.update(mutation)
    }

    private fun likeProject() {
        viewModelScope.launch {
            likeProjectUseCase(uiState.value.project.id).collect { result ->
                result.fold(
                    onSuccess = { isLiked ->
                        updateState { it.copy(isLiked = isLiked) }
                    },
                    onFailure = {
                        snackBarManager.showMessage("Произошла ошибка", SnackbarDuration.Short)
                    }
                )
            }
        }
    }

    fun handleIntent(intent: DetailedProjectIntent) {
        when (intent) {
            DetailedProjectIntent.LikeProject -> likeProject()
        }
    }
}