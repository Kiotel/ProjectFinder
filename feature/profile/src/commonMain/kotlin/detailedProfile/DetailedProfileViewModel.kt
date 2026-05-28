package detailedProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import detailedProfile.models.DetailedProfileState
import detailedProfile.models.InternalDetailedProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import useCases.DeleteAccountUseCase
import useCases.GetUserProfileUseCase

internal class DetailedProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {
    private val _internalState = MutableStateFlow(InternalDetailedProfileState())

    val uiState: StateFlow<DetailedProfileState> = _internalState.map { DetailedProfileState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailedProfileState(InternalDetailedProfileState()),
        )

    init {
        loadProfile()
    }

    fun handleIntent(intent: DetailedProfileIntent, onDeleteSuccess: () -> Unit = {}) {
        when (intent) {
            DetailedProfileIntent.DeleteAccount -> deleteAccount(onDeleteSuccess)
            else -> {}
        }
    }

    private fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            deleteAccountUseCase().onSuccess {
                onSuccess()
            }.onFailure { error ->
                _internalState.update { it.copy(error = "Не удалось удалить аккаунт: ${error.message}") }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _internalState.update { it.copy(isLoading = true, error = null) }
            getUserProfileUseCase.current().collect { result ->
                result.fold(
                    onSuccess = { profile ->
                        _internalState.update { it.copy(isLoading = false, profile = profile) }
                    },
                    onFailure = { error ->
                        _internalState.update {
                            it.copy(isLoading = false, error = error.message ?: "Ошибка загрузки")
                        }
                    },
                )
            }
        }
    }
}
