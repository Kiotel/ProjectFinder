package registration.models

import org.jetbrains.compose.resources.StringResource

internal data class RegistrationState(
    val email: String,
    val emailErrorText: StringResource?,
    val login: String,
    val loginErrorText: StringResource?,
    val password: String,
    val passwordErrorText: StringResource?,
    val passwordCopy: String,
    val passwordCopyErrorText: StringResource?,
    val consent: Boolean,
    val consentErrorText: StringResource?,
    val isAuthed: Boolean
) {
    internal constructor(internalRegistrationState: InternalRegistrationState) : this(
        email = internalRegistrationState.email,
        emailErrorText = internalRegistrationState.emailErrorText,
        login = internalRegistrationState.login,
        loginErrorText = internalRegistrationState.loginErrorText,
        password = internalRegistrationState.password,
        passwordErrorText = internalRegistrationState.passwordErrorText,
        passwordCopy = internalRegistrationState.passwordCopy,
        passwordCopyErrorText = internalRegistrationState.passwordCopyErrorText,
        consent = internalRegistrationState.consent,
        consentErrorText = internalRegistrationState.consentErrorText,
        isAuthed = internalRegistrationState.isAuthed
    )
}
