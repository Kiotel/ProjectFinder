package registration

internal sealed interface RegistrationIntent {
    data class SetEmail(val newEmail: String) : RegistrationIntent
    data class SetLogin(val newLogin: String) : RegistrationIntent
    data class SetPassword(val newPassword: String) : RegistrationIntent
    data class SetPasswordCopy(val newPasswordCopy: String) : RegistrationIntent
    data class SetConsent(val newConsent: Boolean) : RegistrationIntent
    object OnRegister : RegistrationIntent
}