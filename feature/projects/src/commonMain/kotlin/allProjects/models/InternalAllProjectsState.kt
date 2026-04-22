package allProjects.models

import org.jetbrains.compose.resources.StringResource


internal data class InternalAllProjectsState(
    val email: String = "",
    val emailErrorText: StringResource? = null,
    val projects: String = "",
    val projectsErrorText: StringResource? = null,
    val password: String = "",
    val passwordErrorText: StringResource? = null,
    val passwordCopy: String = "",
    val passwordCopyErrorText: StringResource? = null,
    val consent: Boolean = false,
    val consentErrorText: StringResource? = null,
    val isAuthed: Boolean = false
)
