package login

import androidx.compose.ui.input.key.Key.Companion.D
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import login.models.InternalLoginState
import login.models.LoginState
import projectfinder.core.ui.generated.resources.Res
import projectfinder.core.ui.generated.resources.error_login_length
import projectfinder.core.ui.generated.resources.snackbar_login_in_progress
import projectfinder.core.ui.generated.resources.snackbar_login_success
import useCases.RegisterUseCase

internal class LoginViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalLoginState()
    )


    val uiState: StateFlow<LoginState> = _internalState.map { internalState ->
        LoginState(
            email = internalState.email,
            password = internalState.password,
            authed = internalState.authed,
            snackBarMessageResource = internalState.snackBarMessageResource,
            currentSnackBarMessageId = internalState.currentSnackBarMessageId
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
            updateState {
                it.copy(
                    snackBarMessageResource = Res.string.snackbar_login_in_progress,
                    currentSnackBarMessageId = uiState.value.currentSnackBarMessageId + 1
                )
            }
            delay(3000)
            // login process
            updateState {
                it.copy(
                    authed = true,
                    snackBarMessageResource = Res.string.snackbar_login_success,
                    currentSnackBarMessageId = uiState.value.currentSnackBarMessageId + 1
                )
            }
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