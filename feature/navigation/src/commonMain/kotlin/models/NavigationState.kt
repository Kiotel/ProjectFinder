package models

internal data class NavigationState(
    val isAuthed: Boolean,
    val userProfile: UserProfile? = null,
    val isLoading: Boolean,
    val isInitialCheckFinished: Boolean
) {
    internal constructor(internalRegistrationState: InternalNavigationState) : this(
        isAuthed = internalRegistrationState.isAuthed,
        userProfile = internalRegistrationState.userProfile,
        isLoading = internalRegistrationState.isLoading,
        isInitialCheckFinished = internalRegistrationState.isInitialCheckFinished
    )
}
