package notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import useCases.GetNotificationsUseCase

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val uiState: StateFlow<NotificationsState> = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotificationsState(),
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getNotificationsUseCase().fold(
                onSuccess = { text ->
                    _state.update { it.copy(isLoading = false, content = text) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Не удалось загрузить уведомления",
                        )
                    }
                },
            )
        }
    }
}

data class NotificationsState(
    val isLoading: Boolean = true,
    val content: String = "",
    val error: String? = null,
)
