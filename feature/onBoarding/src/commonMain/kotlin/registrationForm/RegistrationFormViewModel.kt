package registrationForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import projectfinder.core.ui.generated.resources.Res
import projectfinder.core.ui.generated.resources.error_email_length
import projectfinder.core.ui.generated.resources.error_email_wrong
import projectfinder.core.ui.generated.resources.error_login_length
import projectfinder.core.ui.generated.resources.error_password_and_passord_copy_not_equal
import projectfinder.core.ui.generated.resources.error_password_length
import registrationForm.models.InternalRegistrationState
import registrationForm.models.RegistrationState

internal class RegistrationFormViewModel : ViewModel() {
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
            registrationErrorText = internalState.registrationErrorText
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
            updateState { it.copy(passwordErrorText = null) }
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