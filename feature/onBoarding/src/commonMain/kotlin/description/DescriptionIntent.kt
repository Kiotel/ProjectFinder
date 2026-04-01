package description

internal sealed interface DescriptionIntent {
    data class SetEmail(val newEmail: String) : DescriptionIntent
    data class SetLogin(val newLogin: String) : DescriptionIntent
    data class SetPassword(val newPassword: String) : DescriptionIntent
    data class SetPasswordCopy(val newPasswordCopy: String) : DescriptionIntent
    data class SetConsent(val newConsent: Boolean) : DescriptionIntent
    object OnRegister : DescriptionIntent
}