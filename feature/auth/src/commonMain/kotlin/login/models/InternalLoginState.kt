package login.models

internal data class InternalLoginState(
    val email: String = "",
    val password: String = "",
    val isAuthed: Boolean = false,
)
