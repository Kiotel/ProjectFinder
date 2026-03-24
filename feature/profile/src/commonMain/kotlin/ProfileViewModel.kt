import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import useCases.RegisterUseCase

internal class ProfileViewModel(
    private val setTokenUseCase: RegisterUseCase,
) : ViewModel() {

    private val _localState = MutableStateFlow(InternalProfileState())
    /*
        val uiState: StateFlow<InternalOnboardingState> = combine(
            getTokenUseCase(), _localState
        ) { token, localState ->
            localState.copy(token = token)
        }.catch { t ->
            _localState.update { it.copy(errorMsg = t.message, isLoading = false) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InternalOnboardingState(isLoading = true)
        )

        private fun setToken() {
            viewModelScope.launch {
                _localState.update { it.copy(isLoading = true) }
                try {
                    setTokenUseCase((uiState.value.token ?: "0").toInt().plus(1).toString())
                } catch (e: Exception) {
                    _localState.update { it.copy(errorMsg = e.message) }
                } finally {
                    _localState.update { it.copy(isLoading = false) }
                }
            }
        }

     */

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.SetToken -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("ProfileViewModel: cleared")
    }
}