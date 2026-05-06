package allProjects.models

internal data class AllProjectsState(
    val isAuthed: Boolean
) {
    internal constructor(internalRegistrationState: InternalAllProjectsState) : this(
        isAuthed = internalRegistrationState.isAuthed
    )
}
