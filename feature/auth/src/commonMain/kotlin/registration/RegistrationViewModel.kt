package registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import projectfinder.core.ui.generated.resources.Res
import projectfinder.core.ui.generated.resources.error_email_length
import projectfinder.core.ui.generated.resources.error_email_wrong
import projectfinder.core.ui.generated.resources.error_login_length
import projectfinder.core.ui.generated.resources.error_password_and_passord_copy_not_equal
import projectfinder.core.ui.generated.resources.error_password_length
import projectfinder.core.ui.generated.resources.error_password_must_contain_digits_and_letter
import projectfinder.core.ui.generated.resources.snackbar_registration_failed
import projectfinder.core.ui.generated.resources.snackbar_registration_in_progress
import projectfinder.core.ui.generated.resources.snackbar_registration_success
import registration.models.InternalRegistrationState
import registration.models.RegistrationState
import useCases.RegisterUseCase

internal class RegistrationViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalRegistrationState()
    )


    val uiState: StateFlow<RegistrationState> = _internalState.map { internalState ->
        RegistrationState(
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
        initialValue = RegistrationState(InternalRegistrationState())
    )

    private fun validateForm(): Boolean {
        var isValid = true
        if (uiState.value.email.length < 3) {
            isValid = false
            updateState { it.copy(emailErrorText = Res.string.error_email_length) }
        } else if (!uiState.value.email.contains("@")) {
            isValid = false
            updateState { it.copy(emailErrorText = Res.string.error_email_wrong) }
        } else {
            updateState { it.copy(emailErrorText = null) }
        }

        if (uiState.value.login.length < 3) {
            isValid = false
            updateState { it.copy(loginErrorText = Res.string.error_login_length) }
        } else {
            updateState { it.copy(loginErrorText = null) }
        }

        if (uiState.value.password.length < 8) {
            isValid = false
            updateState { it.copy(passwordErrorText = Res.string.error_password_length) }
        } else {
            if (!uiState.value.password.toCharArray()
                    .any { it.isLetter() } || !uiState.value.password.toCharArray()
                    .any { it.isDigit() }
            ) {
                updateState { it.copy(passwordErrorText = Res.string.error_password_must_contain_digits_and_letter) }
            } else {
                updateState { it.copy(passwordErrorText = null) }
            }
        }

        if (uiState.value.password != uiState.value.passwordCopy) {
            isValid = false
            updateState { it.copy(passwordCopyErrorText = Res.string.error_password_and_passord_copy_not_equal) }
        } else {
            updateState { it.copy(passwordCopyErrorText = null) }
        }
        return isValid
    }

    private fun onRegister() {
        val isFormValid = validateForm()

        viewModelScope.launch {
            if (isFormValid) {
                updateState {
                    it.copy(
                        snackBarMessageResource = Res.string.snackbar_registration_in_progress,
                        currentSnackBarMessageId = it.currentSnackBarMessageId + 1
                    )
                }
                registerUseCase(
                    username = uiState.value.login,
                    email = uiState.value.email,
                    password = uiState.value.password
                ).collect { result ->
                    result.onSuccess {
                        updateState {
                            it.copy(
                                snackBarMessageResource = Res.string.snackbar_registration_success,
                                currentSnackBarMessageId = it.currentSnackBarMessageId + 1
                            )
                        }
                    }.onFailure {
                        updateState {
                            it.copy(
                                snackBarMessageResource = Res.string.snackbar_registration_failed,
                                currentSnackBarMessageId = it.currentSnackBarMessageId + 1
                            )
                        }
                    }
                }
            }
        }
    }

    internal fun updateState(mutation: (InternalRegistrationState) -> InternalRegistrationState) {
        _internalState.update(mutation)
    }


    fun handleIntent(intent: RegistrationIntent) {
        when (intent) {
            RegistrationIntent.OnRegister -> onRegister()
            is RegistrationIntent.SetConsent -> updateState { it.copy(consent = intent.newConsent) }
            is RegistrationIntent.SetEmail -> updateState { it.copy(email = intent.newEmail) }
            is RegistrationIntent.SetLogin -> updateState { it.copy(login = intent.newLogin) }
            is RegistrationIntent.SetPassword -> updateState { it.copy(password = intent.newPassword) }
            is RegistrationIntent.SetPasswordCopy -> updateState { it.copy(passwordCopy = intent.newPasswordCopy) }
        }
    }
}