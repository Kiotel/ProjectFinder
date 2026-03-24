package detailedProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import detailedProfile.models.DetailedProfileState
import detailedProfile.models.InternalDetailedProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import useCases.RegisterUseCase

internal class DetailedProfileViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalDetailedProfileState()
    )


    val uiState: StateFlow<DetailedProfileState> = _internalState.map { internalState ->
        DetailedProfileState(
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
        initialValue = DetailedProfileState(InternalDetailedProfileState())
    )

    internal fun updateState(mutation: (InternalDetailedProfileState) -> InternalDetailedProfileState) {
        _internalState.update(mutation)
    }


    fun handleIntent(intent: DetailedProfileIntent) {
        when (intent) {
            DetailedProfileIntent.OnRegister -> {}
            is DetailedProfileIntent.SetConsent -> updateState { it.copy(consent = intent.newConsent) }
            is DetailedProfileIntent.SetEmail -> updateState { it.copy(email = intent.newEmail) }
            is DetailedProfileIntent.SetLogin -> updateState { it.copy(login = intent.newLogin) }
            is DetailedProfileIntent.SetPassword -> updateState { it.copy(password = intent.newPassword) }
            is DetailedProfileIntent.SetPasswordCopy -> updateState { it.copy(passwordCopy = intent.newPasswordCopy) }
        }
    }
}