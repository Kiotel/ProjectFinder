import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import models.InternalNavigationState
import models.NavigationState

internal class NavigationViewModel(
) : ViewModel() {

    private val _internalState = MutableStateFlow(
        InternalNavigationState()
    )

    val uiState: StateFlow<NavigationState> = _internalState.map { internalState ->
        NavigationState(
            isAuthed = internalState.isAuthed,
            isLoading = internalState.isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NavigationState(InternalNavigationState())
    )

    private fun updateState(mutation: (InternalNavigationState) -> InternalNavigationState) {
        _internalState.update(mutation)
    }

    fun handleIntent(intent: NavigationIntent) {
        when (intent) {
            else -> {}
        }
    }
}