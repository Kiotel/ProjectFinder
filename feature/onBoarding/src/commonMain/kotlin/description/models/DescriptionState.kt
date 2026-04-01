package description.models

import org.jetbrains.compose.resources.StringResource

internal data class DescriptionState(
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
    internal constructor(internalDescriptionState: InternalDescriptionState) : this(
        email = internalDescriptionState.email,
        emailErrorText = internalDescriptionState.emailErrorText,
        login = internalDescriptionState.login,
        loginErrorText = internalDescriptionState.loginErrorText,
        password = internalDescriptionState.password,
        passwordErrorText = internalDescriptionState.passwordErrorText,
        passwordCopy = internalDescriptionState.passwordCopy,
        passwordCopyErrorText = internalDescriptionState.passwordCopyErrorText,
        consent = internalDescriptionState.consent,
        consentErrorText = internalDescriptionState.consentErrorText,
        isAuthed = internalDescriptionState.isAuthed
    )
}
