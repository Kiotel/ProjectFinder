package registrationForm.models

internal data class InternalRegistrationState(
    val email: String = "",
    val login: String = "",
    val password: String = "",
    val passwordCopy: String = "",
    val consent: Boolean = false,
)
