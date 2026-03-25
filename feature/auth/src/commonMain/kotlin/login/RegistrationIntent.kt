package login

internal sealed interface LoginIntent {
    data class SetEmail(val newEmail: String) :LoginIntent
    data class SetLogin(val newLogin: String) :LoginIntent
    data class SetPassword(val newPassword: String) :LoginIntent
    data class SetPasswordCopy(val newPasswordCopy: String) :LoginIntent
    data class SetConsent(val newConsent: Boolean) :LoginIntent
    object OnRegister :LoginIntent
}