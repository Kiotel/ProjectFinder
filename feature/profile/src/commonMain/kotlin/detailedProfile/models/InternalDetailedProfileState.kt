package detailedProfile.models

import models.UserProfile

internal data class InternalDetailedProfileState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val error: String? = null,
)
