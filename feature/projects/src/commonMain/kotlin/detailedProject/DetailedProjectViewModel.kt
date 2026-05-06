package detailedProject

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
import models.Project
import useCases.GetProjectsUseCase
import useCases.RegisterUseCase
import utils.SnackBarManager

internal class DetailedProjectViewModel(
    private val registerUseCase: RegisterUseCase,
    private val getProjectUseCase: GetProjectsUseCase,
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


    fun handleIntent(intent: DetailedProjectIntent) {
        when (intent) {
            else -> {}
        }
    }
}