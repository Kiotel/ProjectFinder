package allProjects.models

import org.jetbrains.compose.resources.StringResource

internal data class AllProjectsState(
    val email: String,
    val emailErrorText: StringResource?,
    val projects: String,
    val projectsErrorText: StringResource?,
    val password: String,
    val passwordErrorText: StringResource?,
    val passwordCopy: String,
    val passwordCopyErrorText: StringResource?,
    val consent: Boolean,
    val consentErrorText: StringResource?,
    val isAuthed: Boolean
) {
    internal constructor(internalRegistrationState: InternalAllProjectsState) : this(
        email = internalRegistrationState.email,
        emailErrorText = internalRegistrationState.emailErrorText,
        projects = internalRegistrationState.projects,
        projectsErrorText = internalRegistrationState.projectsErrorText,
        password = internalRegistrationState.password,
        passwordErrorText = internalRegistrationState.passwordErrorText,
        passwordCopy = internalRegistrationState.passwordCopy,
        passwordCopyErrorText = internalRegistrationState.passwordCopyErrorText,
        consent = internalRegistrationState.consent,
        consentErrorText = internalRegistrationState.consentErrorText,
        isAuthed = internalRegistrationState.isAuthed
    )
}
