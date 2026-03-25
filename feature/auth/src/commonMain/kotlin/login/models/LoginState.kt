package login.models

import org.jetbrains.compose.resources.StringResource

internal data class LoginState(
    val email: String,
    val password: String,
    val authed: Boolean,
    val snackBarMessageResource: StringResource?,
    val currentSnackBarMessageId: Int,
) {
    internal constructor(internalRegistrationState: InternalLoginState) : this(
        email = internalRegistrationState.email,
        password = internalRegistrationState.password,
        authed = internalRegistrationState.authed,
        snackBarMessageResource = internalRegistrationState.snackBarMessageResource,
        currentSnackBarMessageId = internalRegistrationState.currentSnackBarMessageId
    )
}
