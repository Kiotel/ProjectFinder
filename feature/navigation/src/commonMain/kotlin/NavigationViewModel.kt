import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
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
import useCases.GetUserProfileUseCase
import useCases.LogoutUseCase

internal class NavigationViewModel(
    private val getIsAuthedUseCase: GetIsAuthedUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val profileFillManager: ProfileFillManager,
) : ViewModel() {

    private val _internalState = MutableStateFlow(InternalNavigationState())
    private var authJob: Job? = null
    private var profileJob: Job? = null

    init {
        updateState { it.copy(isProfileFilledLocally = profileFillManager.isFilled) }
        checkIsAuthed()
    }

    val uiState: StateFlow<NavigationState> = _internalState.map { internalState ->
        NavigationState(
            isAuthed = internalState.isAuthed,
            userProfile = internalState.userProfile,
            isLoading = internalState.isLoading,
            isInitialCheckFinished = internalState.isInitialCheckFinished,
            isProfileFilledLocally = internalState.isProfileFilledLocally
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NavigationState(_internalState.value)
    )

    private fun updateState(mutation: (InternalNavigationState) -> InternalNavigationState) {
        _internalState.update(mutation)
    }

    private fun checkIsAuthed() {
        authJob?.cancel()
        profileJob?.cancel()
        authJob = viewModelScope.launch {
            getIsAuthedUseCase().collect { result ->
                result.onSuccess {
                    updateState { it.copy(isAuthed = true, isLoading = true) }
                    fetchUserProfile()
                }
                result.onFailure { error ->
                    updateState { it.copy(isAuthed = false, isLoading = false, isInitialCheckFinished = true) }
                    println("USER IS NOT AUTHED. Error: ${error.stackTraceToString()}")
                }
            }
        }
    }

    private fun fetchUserProfile() {
        profileJob?.cancel()
        profileJob = viewModelScope.launch {
            getUserProfileUseCase.current().collect { result ->
                result.onSuccess { profile ->
                    println("NavigationViewModel/fetchUserProfile: SUCCESS. profile=$profile, isFilled=${profile.isProfileFilled}, localFilled=${profileFillManager.isFilled}")
                    updateState {
                        it.copy(
                            userProfile = profile,
                            isLoading = false,
                            isInitialCheckFinished = true,
                            isProfileFilledLocally = profileFillManager.isFilled
                        )
                    }
                }
                result.onFailure { error ->
                    println("NavigationViewModel/fetchUserProfile: FAILURE. error=${error.message}")
                    updateState {
                        it.copy(
                            isLoading = false,
                            isInitialCheckFinished = true,
                            isProfileFilledLocally = profileFillManager.isFilled
                        )
                    }
                }
            }
        }
    }

    private fun logout() {
        authJob?.cancel()
        profileJob?.cancel()
        authJob = null
        profileJob = null
        viewModelScope.launch {
            logoutUseCase()
            profileFillManager.clear()
            updateState { InternalNavigationState(isAuthed = false, isLoading = false, isInitialCheckFinished = true) }
        }
    }

    /**
     * После успешного удаления аккаунта на сервере (выполняется в DetailedProfileViewModel)
     * NavigationViewModel получает сигнал DeleteAccount и просто чистит локальное состояние.
     * Повторный вызов deleteAccountUseCase() не нужен — токены уже сброшены.
     */
    private fun deleteAccount() {
        authJob?.cancel()
        profileJob?.cancel()
        authJob = null
        profileJob = null
        viewModelScope.launch {
            profileFillManager.clear()
            updateState { InternalNavigationState(isAuthed = false, isLoading = false, isInitialCheckFinished = true) }
        }
    }

    fun handleIntent(intent: NavigationIntent) {
        when (intent) {
            NavigationIntent.Logout -> logout()
            NavigationIntent.DeleteAccount -> deleteAccount()
        }
    }
}
