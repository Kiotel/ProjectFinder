package participantProfile

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.UserProfile
import useCases.BookmarkUserUseCase
import useCases.GetUserProfileUseCase
import utils.SnackBarManager

class ParticipantProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val bookmarkUserUseCase: BookmarkUserUseCase,
    val snackBarManager: SnackBarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(ParticipantProfileState())
    val uiState: StateFlow<ParticipantProfileState> = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ParticipantProfileState(),
    )

    fun load(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getUserProfileUseCase.byId(userId).fold(
                onSuccess = { profile ->
                    _state.update { it.copy(isLoading = false, profile = profile) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message ?: "Ошибка загрузки")
                    }
                },
            )
        }
    }

    fun bookmark() {
        val userId = _state.value.profile?.id ?: return
        viewModelScope.launch {
            bookmarkUserUseCase(userId).fold(
                onSuccess = {
                    snackBarManager.showMessage("Добавлено в избранное", SnackbarDuration.Short)
                },
                onFailure = {
                    snackBarManager.showMessage(
                        it.message ?: "Не удалось добавить в избранное",
                        SnackbarDuration.Short,
                    )
                },
            )
        }
    }
}

data class ParticipantProfileState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val error: String? = null,
)
