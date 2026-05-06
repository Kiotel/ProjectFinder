package allProjects

import allProjects.models.AllProjectsState
import allProjects.models.InternalAllProjectsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import useCases.GetProjectsUseCase
import useCases.RegisterUseCase
import utils.SnackBarManager

internal class AllProjectsViewModel(
    private val registerUseCase: RegisterUseCase,
    private val getProjectUseCase: GetProjectsUseCase,
    val snackBarManager: SnackBarManager
) : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalAllProjectsState()
    )

    val projectsFlow = Pager(
        PagingConfig(pageSize = 5)
    ) {
        ProjectsPagingSource(
            getProjectsUseCase = getProjectUseCase,
            query = "s"
        )
    }.flow.cachedIn(viewModelScope)
    val uiState: StateFlow<AllProjectsState> = _internalState.map { internalState ->
        AllProjectsState(
            isAuthed = internalState.isAuthed
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AllProjectsState(InternalAllProjectsState())
    )
    private fun updateState(mutation: (InternalAllProjectsState) -> InternalAllProjectsState) {
        _internalState.update(mutation)
    }

    fun handleIntent(intent: AllProjectsIntent) {
        when (intent) {
            else -> {}
        }
    }
}