package models

internal data class NavigationState(
    val isAuthed: Boolean,
    val isLoading: Boolean
) {
    internal constructor(internalRegistrationState: InternalNavigationState) : this(
        isAuthed = internalRegistrationState.isAuthed,
        isLoading = internalRegistrationState.isLoading
    )
}
