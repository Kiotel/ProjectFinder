package allProjects

internal sealed interface AllProjectsIntent {
    data class SetEmail(val newEmail: String) : AllProjectsIntent
    data class SetProjects(val newProjects: String) : AllProjectsIntent
    data class SetPassword(val newPassword: String) : AllProjectsIntent
    data class SetPasswordCopy(val newPasswordCopy: String) : AllProjectsIntent
    data class SetConsent(val newConsent: Boolean) : AllProjectsIntent
    object OnRegister : AllProjectsIntent
}