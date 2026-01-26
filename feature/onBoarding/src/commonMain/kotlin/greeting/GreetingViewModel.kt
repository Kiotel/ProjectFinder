import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import greeting.GreetingIntent
import greeting.GreetingScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import useCases.GetIsEverLoggedUseCase
import useCases.GetIsFilledDescriptionFormUseCase
import useCases.SetIsEverLoggedUseCase
import useCases.SetIsFilledDescriptionForm

class GreetingViewModel(
    private val setIsFilledDescriptionFormUseCase: SetIsFilledDescriptionForm,
    private val setIsEverLoggedUseCase: SetIsEverLoggedUseCase,
    private val getIsFilledDescriptionFormUseCase: GetIsFilledDescriptionFormUseCase,
    private val getIsEverLoggedUseCase: GetIsEverLoggedUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GreetingScreenState(
            isLoading = true,
            errorMsg = null,
            isFilledDescriptionForm = false,
            firstTime = false
        )
    )
    val uiState: StateFlow<GreetingScreenState> = _uiState.asStateFlow()

    init {
        observeInitializationData()
    }

    private fun observeInitializationData() {
        combine(
            getIsFilledDescriptionFormUseCase(),
            getIsEverLoggedUseCase()
        ) { isFilled, isEverLogged ->
            _uiState.update {
                it.copy(
                    isFilledDescriptionForm = isFilled,
                    firstTime = !isEverLogged,
                    isLoading = false
                )
            }
        }.catch { t ->
            _uiState.update { it.copy(errorMsg = t.message, isLoading = false) }
        }.launchIn(viewModelScope)
    }

    fun onCompleteProfileClicked(formData: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                setIsFilledDescriptionFormUseCase(true)
                setIsEverLoggedUseCase(true)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMsg = "Failed to save") }
            }
        }
    }

    fun toggleEverLogged(){
        viewModelScope.launch {
            _uiState
        }
    }

    fun handleIntent(intent: GreetingIntent) {
        when (intent){
            GreetingIntent.toggleEverLogged ->
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("GreetingViewModel: cleared")
    }
}