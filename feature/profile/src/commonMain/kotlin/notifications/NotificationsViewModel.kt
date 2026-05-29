package notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.Notification
import useCases.GetNotificationsUseCase

data class NotificationsState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val error: String? = null,
)

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
        load()
    }

    fun refresh() {
        if (_state.value.isRefreshing || _state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            fetchNotifications()
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            fetchNotifications()
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun fetchNotifications() {
        getNotificationsUseCase().fold(
            onSuccess = { list ->
                _state.update {
                    it.copy(
                        notifications = list,
                        unreadCount = list.count { n -> !n.isRead },
                        error = null,
                    )
                }
            },
            onFailure = { error ->
                _state.update {
                    it.copy(
                        error = error.message ?: "Не удалось загрузить уведомления",
                    )
                }
            },
        )
    }
}
