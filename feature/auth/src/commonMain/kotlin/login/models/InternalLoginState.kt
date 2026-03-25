package login.models

import org.jetbrains.compose.resources.StringResource

internal data class InternalLoginState(
    val email: String = "",
    val password: String = "",
    val authed: Boolean = false,
    val snackBarMessageResource: StringResource? = null,
    val currentSnackBarMessageId: Int = 0,
)
