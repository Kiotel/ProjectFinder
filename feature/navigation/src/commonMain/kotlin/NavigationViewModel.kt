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
import useCases.DeleteAccountUseCase
import useCases.GetIsAuthedUseCase
import useCases.GetProjectsUseCase
import useCases.GetUserProfileUseCase
import useCases.LogoutUseCase

internal class NavigationViewModel(
    private val getIsAuthedUseCase: GetIsAuthedUseCase,
    private val getProjectsUseCase: GetProjectsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    private val _internalState = MutableStateFlow(InternalNavigationState())
    private var authJob: Job? = null
    private var profileJob: Job? = null

    init {
        checkIsAuthed()
    }

    val uiState: StateFlow<NavigationState> = _internalState.map { internalState ->
        NavigationState(
            isAuthed = internalState.isAuthed,
            userProfile = internalState.userProfile,
            isLoading = internalState.isLoading,
            isInitialCheckFinished = internalState.isInitialCheckFinished
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
                    println("NavigationViewModel/fetchUserProfile: SUCCESS. profile=$profile, isFilled=${profile.isProfileFilled}")
                    updateState { it.copy(userProfile = profile, isLoading = false, isInitialCheckFinished = true) }
                }
                result.onFailure { error ->
                    println("NavigationViewModel/fetchUserProfile: FAILURE. error=${error.message}")
                    updateState { it.copy(isLoading = false, isInitialCheckFinished = true) }
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
            updateState { InternalNavigationState(isAuthed = false, isLoading = false, isInitialCheckFinished = true) }
        }
    }

    private fun deleteAccount() {
        authJob?.cancel()
        profileJob?.cancel()
        authJob = null
        profileJob = null
        viewModelScope.launch {
            deleteAccountUseCase().onSuccess {
                updateState { InternalNavigationState(isAuthed = false, isLoading = false, isInitialCheckFinished = true) }
            }.onFailure { error ->
                println("NavigationViewModel/deleteAccount: FAILURE. error=${error.message}")
            }
        }
    }

    private fun getProjects() {
        viewModelScope.launch {
            getProjectsUseCase(page = 1, limit = 50).collect { it ->
                it.onSuccess { projects ->
                    println(
                        "NavigationViewModel/getProjects. Got projects: ${
                            projects.map {
                                "\nid ${it.id}\n" +
                                "title ${it.title}\n" +
                                "authorName ${it.authorName}\n" +
                                "updateAt ${it.updatedAt}\n" +
                                "createdAt ${it.createdAt}\n"
                            }
                        }"
                    )
                }
                it.onFailure { e ->
                    println("NavigationViewModel/getProjects. Got failure: $e")
                }
            }
        }
    }

    fun handleIntent(intent: NavigationIntent) {
        when (intent) {
            NavigationIntent.CheckIsAuthed -> checkIsAuthed()
            NavigationIntent.GetProjects -> getProjects()
            NavigationIntent.Logout -> logout()
            NavigationIntent.DeleteAccount -> deleteAccount()
        }
    }
}
