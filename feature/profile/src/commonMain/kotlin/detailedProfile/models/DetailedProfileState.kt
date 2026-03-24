package detailedProfile.models

import org.jetbrains.compose.resources.StringResource

internal data class DetailedProfileState(
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
    val snackBarMessageResource: StringResource?,
    val currentSnackBarMessageId: Int,
) {
    internal constructor(internalProfileState: InternalDetailedProfileState) : this(
        email = internalProfileState.email,
        emailErrorText = internalProfileState.emailErrorText,
        login = internalProfileState.login,
        loginErrorText = internalProfileState.loginErrorText,
        password = internalProfileState.password,
        passwordErrorText = internalProfileState.passwordErrorText,
        passwordCopy = internalProfileState.passwordCopy,
        passwordCopyErrorText = internalProfileState.passwordCopyErrorText,
        consent = internalProfileState.consent,
        consentErrorText = internalProfileState.consentErrorText,
        snackBarMessageResource = internalProfileState.snackBarMessageResource,
        currentSnackBarMessageId = internalProfileState.currentSnackBarMessageId
    )
}
