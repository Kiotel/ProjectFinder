package detailedProject.models

import androidx.compose.runtime.Stable
import models.Project

@Stable
internal data class DetailedProjectState(
    val isLiked: Boolean,
    val project: Project
) {
    internal constructor(
        internalState: InternalDetailedProjectState
    ) : this(
        isLiked = internalState.isLiked,
        project = internalState.project
    )
}
