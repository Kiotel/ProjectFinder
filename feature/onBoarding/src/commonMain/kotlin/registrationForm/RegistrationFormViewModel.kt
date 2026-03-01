package registrationForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import registrationForm.models.InternalRegistrationState
import registrationForm.models.RegistrationState

internal class RegistrationFormViewModel : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalRegistrationState()
    )
    val uiState: StateFlow<RegistrationState> = _internalState.map { internalState ->
        RegistrationState(
            email = internalState.email,
            login = internalState.login,
            password = internalState.password,
            passwordCopy = internalState.passwordCopy,
            consent = internalState.consent
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RegistrationState(InternalRegistrationState())
    )

    internal fun updateState(mutation: (InternalRegistrationState) -> InternalRegistrationState) {
        _internalState.update(mutation)
    }


    fun handleIntent(intent: RegistrationIntent) {
        when (intent) {
            RegistrationIntent.OnRegister -> {}
            is RegistrationIntent.SetConsent -> updateState { it.copy(consent = intent.newConsent) }
            is RegistrationIntent.SetEmail -> updateState { it.copy(email = intent.newEmail) }
            is RegistrationIntent.SetLogin -> updateState { it.copy(login = intent.newLogin) }
            is RegistrationIntent.SetPassword -> updateState { it.copy(password = intent.newPassword) }
            is RegistrationIntent.SetPasswordCopy -> updateState { it.copy(passwordCopy = intent.newPasswordCopy) }
        }
    }
}