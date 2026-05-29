package models


internal data class InternalNavigationState(
    val isAuthed: Boolean = false,
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = true,
    val isInitialCheckFinished: Boolean = false,
    val isProfileFilledLocally: Boolean = false
)
