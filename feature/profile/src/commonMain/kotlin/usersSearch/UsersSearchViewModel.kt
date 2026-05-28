package usersSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.UserProfile
import useCases.SearchUsersUseCase

class UsersSearchViewModel(
    private val searchUsersUseCase: SearchUsersUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(UsersSearchState())
    val uiState: StateFlow<UsersSearchState> = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UsersSearchState(),
    )

    private var searchJob: Job? = null

    init {
        search("")
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            search(query)
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            searchUsersUseCase(query).fold(
                onSuccess = { page ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            users = page.users,
                            totalItems = page.totalItems,
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Ошибка поиска",
                            users = emptyList(),
                        )
                    }
                },
            )
        }
    }
}

data class UsersSearchState(
    val query: String = "",
    val users: List<UserProfile> = emptyList(),
    val totalItems: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
)
