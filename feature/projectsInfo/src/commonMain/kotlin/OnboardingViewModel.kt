import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import useCases.GetTokenUseCase
import useCases.SetTokenUseCase

internal class OnboardingViewModel(
    private val setTokenUseCase: SetTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
) : ViewModel() {

    private val _localState = MutableStateFlow(OnBoardingState())

    val uiState: StateFlow<OnBoardingState> = combine(
        getTokenUseCase(), _localState
    ) { token, localState ->
        localState.copy(token = token)
    }.catch { t ->
        _localState.update { it.copy(errorMsg = t.message, isLoading = false) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OnBoardingState(isLoading = true)
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

    fun handleIntent(intent: OnBoardingIntent) {
        when (intent) {
            is OnBoardingIntent.SetToken -> setToken()
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("OnBoardingViewModel: cleared")
    }
}