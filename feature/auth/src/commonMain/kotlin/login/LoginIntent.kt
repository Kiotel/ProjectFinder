package login

internal sealed interface LoginIntent {
    data class SetEmail(val newEmail: String) :LoginIntent
    data class SetPassword(val newPassword: String) :LoginIntent
    object OnLogin :LoginIntent
}