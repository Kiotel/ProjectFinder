package allProjects

import allProjects.models.AllProjectsState
import allProjects.models.InternalAllProjectsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import useCases.GetProjectsUseCase
import useCases.GetUserProfileUseCase
import utils.SnackBarManager

internal class AllProjectsViewModel(
    private val getProjectUseCase: GetProjectsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    val snackBarManager: SnackBarManager,
) : ViewModel() {

    private val _internalState = MutableStateFlow(InternalAllProjectsState())

    init {
        viewModelScope.launch {
            val userId = getUserProfileUseCase.current().first().getOrNull()?.id ?: ""
            updateState { it.copy(currentUserId = userId) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val projectsFlow = _internalState
        .map { it.searchQuery }
        .flatMapLatest { query ->
            Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
                ProjectsPagingSource(getProjectsUseCase = getProjectUseCase, query = query)
            }.flow
        }
        .cachedIn(viewModelScope)

    val uiState: StateFlow<AllProjectsState> = _internalState.map { AllProjectsState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AllProjectsState(_internalState.value),
        )

    private fun updateState(mutation: (InternalAllProjectsState) -> InternalAllProjectsState) {
        _internalState.update(mutation)
    }

    fun handleIntent(intent: AllProjectsIntent) {
        when (intent) {
            is AllProjectsIntent.UpdateSearchQuery -> updateState { it.copy(searchQuery = intent.query) }
            is AllProjectsIntent.SetFilter -> updateState { it.copy(filter = intent.filter) }
            else -> {}
        }
    }
}
