package login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import login.models.InternalLoginState
import login.models.LoginState
import projectfinder.core.ui.generated.resources.Res
import projectfinder.core.ui.generated.resources.snackbar_login_in_progress
import projectfinder.core.ui.generated.resources.snackbar_login_success
import projectfinder.core.ui.generated.resources.snackbar_login_unknown_error
import useCases.GetUserInfoUseCase
import useCases.LoginUseCase
import utils.SnackBarManager

internal class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    val snackBarManager: SnackBarManager
) : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalLoginState()
    )


    val uiState: StateFlow<LoginState> = _internalState.map { internalState ->
        LoginState(
            email = internalState.email,
            password = internalState.password,
            isAuthed = internalState.isAuthed,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginState(InternalLoginState())
    )

    private fun updateState(mutation: (InternalLoginState) -> InternalLoginState) {
        _internalState.update(mutation)
    }

    private fun onLogin() {
        viewModelScope.launch {
            snackBarManager.showMessage(Res.string.snackbar_login_in_progress)
            loginUseCase(
                email = uiState.value.email,
                password = uiState.value.password
            ).collect { result ->
                result.onSuccess {
                    snackBarManager.showMessage(Res.string.snackbar_login_success)
                    updateState { it.copy(isAuthed = true) }
                }.onFailure {
                    snackBarManager.showMessage(Res.string.snackbar_login_unknown_error)
                }
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            getUserInfoUseCase()
        }
    }

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.OnLogin -> {
                onLogin()
            }

            is LoginIntent.SetEmail -> updateState { it.copy(email = intent.newEmail) }
            is LoginIntent.SetPassword -> updateState { it.copy(password = intent.newPassword) }
        }
    }
}
