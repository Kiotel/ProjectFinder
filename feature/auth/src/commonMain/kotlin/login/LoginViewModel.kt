package login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import login.models.InternalLoginState
import login.models.LoginState
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
            emailErrorText = internalState.emailErrorText,
            login = internalState.login,
            loginErrorText = internalState.loginErrorText,
            password = internalState.password,
            passwordErrorText = internalState.passwordErrorText,
            passwordCopy = internalState.passwordCopy,
            passwordCopyErrorText = internalState.passwordCopyErrorText,
            consent = internalState.consent,
            consentErrorText = internalState.consentErrorText,
            snackBarMessageResource = internalState.snackBarMessageResource,
            currentSnackBarMessageId = internalState.currentSnackBarMessageId
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginState(InternalLoginState())
    )

    internal fun updateState(mutation: (InternalLoginState) -> InternalLoginState) {
        _internalState.update(mutation)
    }


    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.OnRegister -> {}
            is LoginIntent.SetConsent -> updateState { it.copy(consent = intent.newConsent) }
            is LoginIntent.SetEmail -> updateState { it.copy(email = intent.newEmail) }
            is LoginIntent.SetLogin -> updateState { it.copy(login = intent.newLogin) }
            is LoginIntent.SetPassword -> updateState { it.copy(password = intent.newPassword) }
            is LoginIntent.SetPasswordCopy -> updateState { it.copy(passwordCopy = intent.newPasswordCopy) }
        }
    }
}