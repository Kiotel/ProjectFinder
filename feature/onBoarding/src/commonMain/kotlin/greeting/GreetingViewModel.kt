import androidx.lifecycle.ViewModel
import greeting.GreetingIntent
import greeting.GreetingScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class GreetingViewModel(
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GreetingScreenState(
            isLoading = true,
            errorMsg = null
        )
    )
    val uiState: StateFlow<GreetingScreenState> = _uiState.asStateFlow()

    fun handleIntent(intent: GreetingIntent) {
        when (intent) {
            else -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("GreetingViewModel: cleared")
    }
}