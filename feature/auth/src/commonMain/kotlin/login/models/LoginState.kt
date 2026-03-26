package login.models

internal data class LoginState(
    val email: String,
    val password: String,
    val isAuthed: Boolean,
) {
    internal constructor(internalRegistrationState: InternalLoginState) : this(
        email = internalRegistrationState.email,
        password = internalRegistrationState.password,
        isAuthed = internalRegistrationState.isAuthed,
    )
}
