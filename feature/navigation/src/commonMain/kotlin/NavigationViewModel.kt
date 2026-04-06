import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.InternalNavigationState
import models.NavigationState
import useCases.GetIsAuthedUseCase

internal class NavigationViewModel(
    private val getIsAuthedUseCase: GetIsAuthedUseCase
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

    private fun checkIsAuthed() {
        viewModelScope.launch {
            getIsAuthedUseCase().collect { it ->
                it.onSuccess {
                    println("USER IS AUTHED")
                }
                it.onFailure { error ->
                    println("USER IS NOT AUTHED. Error: ${error.stackTraceToString()}")
                }
            }
        }
    }

    fun handleIntent(intent: NavigationIntent) {
        when (intent) {
            NavigationIntent.CheckIsAuthed -> checkIsAuthed()
        }
    }
}