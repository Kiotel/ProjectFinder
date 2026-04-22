package allProjects

import allProjects.models.AllProjectsState
import allProjects.models.InternalAllProjectsState
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
import useCases.RegisterUseCase
import utils.SnackBarManager

internal class AllProjectsViewModel(
    private val registerUseCase: RegisterUseCase,
    val snackBarManager: SnackBarManager
) : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalAllProjectsState()
    )


    val uiState: StateFlow<AllProjectsState> = _internalState.map { internalState ->
        AllProjectsState(
            email = internalState.email,
            emailErrorText = internalState.emailErrorText,
            projects = internalState.projects,
            projectsErrorText = internalState.projectsErrorText,
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
        initialValue = AllProjectsState(InternalAllProjectsState())
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

        if (uiState.value.projects.length < 3) {
            isValid = false
            updateState { it.copy(projectsErrorText = Res.string.error_login_length) }
        } else {
            updateState { it.copy(projectsErrorText = null) }
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
                snackBarManager.showMessage(Res.string.snackbar_registration_in_progress)
                registerUseCase(
                    username = uiState.value.projects,
                    email = uiState.value.email,
                    password = uiState.value.password
                ).collect { result ->
                    result.onSuccess {
                        snackBarManager.showMessage(Res.string.snackbar_registration_success)
                        updateState { it.copy(isAuthed = true) }
                    }.onFailure {
                        snackBarManager.showMessage(Res.string.snackbar_registration_failed)
                    }
                }
            }
        }
    }

    private fun updateState(mutation: (InternalAllProjectsState) -> InternalAllProjectsState) {
        _internalState.update(mutation)
    }


    fun handleIntent(intent: AllProjectsIntent) {
        when (intent) {
            AllProjectsIntent.OnRegister -> onRegister()
            is AllProjectsIntent.SetConsent -> updateState { it.copy(consent = intent.newConsent) }
            is AllProjectsIntent.SetEmail -> updateState { it.copy(email = intent.newEmail) }
            is AllProjectsIntent.SetProjects -> updateState { it.copy(projects = intent.newProjects) }
            is AllProjectsIntent.SetPassword -> updateState { it.copy(password = intent.newPassword) }
            is AllProjectsIntent.SetPasswordCopy -> updateState { it.copy(passwordCopy = intent.newPasswordCopy) }
        }
    }
}