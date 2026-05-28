package detailedProfile.models

import models.UserProfile

internal data class DetailedProfileState(
    val isLoading: Boolean,
    val profile: UserProfile?,
    val error: String?,
) {
    internal constructor(internal: InternalDetailedProfileState) : this(
        isLoading = internal.isLoading,
        profile = internal.profile,
        error = internal.error,
    )
}
