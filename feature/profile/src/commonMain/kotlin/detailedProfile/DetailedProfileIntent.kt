package detailedProfile

internal sealed interface DetailedProfileIntent {
    data class SetEmail(val newEmail: String) : DetailedProfileIntent
    data class SetLogin(val newLogin: String) : DetailedProfileIntent
    data class SetPassword(val newPassword: String) : DetailedProfileIntent
    data class SetPasswordCopy(val newPasswordCopy: String) : DetailedProfileIntent
    data class SetConsent(val newConsent: Boolean) : DetailedProfileIntent
    object OnRegister : DetailedProfileIntent
    object DeleteAccount : DetailedProfileIntent
}
