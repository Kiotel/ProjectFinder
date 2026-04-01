package description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import description.models.DescriptionState
import description.models.InternalDescriptionState
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
import projectfinder.core.ui.generated.resources.error_password_must_contain_digits_and_letter
import useCases.RegisterUseCase
import utils.SnackBarManager

internal class DescriptionViewModel(
    private val registerUseCase: RegisterUseCase,
    val snackBarManager: SnackBarManager
) : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalDescriptionState()
    )


    val uiState: StateFlow<DescriptionState> = _internalState.map { internalState ->
        DescriptionState(
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
            isAuthed = internalState.isAuthed
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DescriptionState(InternalDescriptionState())
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

    }

    private fun updateState(mutation: (InternalDescriptionState) -> InternalDescriptionState) {
        _internalState.update(mutation)
    }


    fun handleIntent(intent: DescriptionIntent) {
        when (intent) {
            DescriptionIntent.OnRegister -> onRegister()
            is DescriptionIntent.SetConsent -> updateState { it.copy(consent = intent.newConsent) }
            is DescriptionIntent.SetEmail -> updateState { it.copy(email = intent.newEmail) }
            is DescriptionIntent.SetLogin -> updateState { it.copy(login = intent.newLogin) }
            is DescriptionIntent.SetPassword -> updateState { it.copy(password = intent.newPassword) }
            is DescriptionIntent.SetPasswordCopy -> updateState { it.copy(passwordCopy = intent.newPasswordCopy) }
        }
    }
}