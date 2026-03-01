package registrationForm.models

internal data class RegistrationState(
    val email: String,
    val login: String,
    val password: String,
    val passwordCopy: String,
    val consent: Boolean,
) {
    internal constructor(internalRegistrationState: InternalRegistrationState) : this(
        email = internalRegistrationState.email,
        login = internalRegistrationState.login,
        password = internalRegistrationState.password,
        passwordCopy = internalRegistrationState.passwordCopy,
        consent = internalRegistrationState.consent,
    )
}
