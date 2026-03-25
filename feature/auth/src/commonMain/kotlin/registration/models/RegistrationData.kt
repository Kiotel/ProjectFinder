package registration.models

import androidx.compose.runtime.Stable

@Stable
internal data class RegistrationData(
    val email: String,
    val emailErrorText: String?,
    val login: String,
    val loginErrorText: String?,
    val password: String,
    val passwordErrorText: String?,
    val passwordCopy: String,
    val passwordCopyErrorText: String?,
    val consent: Boolean,
    val consentErrorText: String?,
    val registrationErrorText: String?
)