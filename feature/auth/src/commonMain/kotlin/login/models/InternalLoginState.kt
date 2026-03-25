package login.models

import org.jetbrains.compose.resources.StringResource

internal data class InternalLoginState(
    val email: String = "",
    val emailErrorText: StringResource? = null,
    val login: String = "",
    val loginErrorText: StringResource? = null,
    val password: String = "",
    val passwordErrorText: StringResource? = null,
    val passwordCopy: String = "",
    val passwordCopyErrorText: StringResource? = null,
    val consent: Boolean = false,
    val consentErrorText: StringResource? = null,
    val snackBarMessageResource: StringResource? = null,
    val currentSnackBarMessageId: Int = 0,
)
