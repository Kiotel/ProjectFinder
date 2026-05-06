package detailedProject.models

import androidx.compose.runtime.Stable
import models.Project

@Stable
internal data class DetailedProjectState(
    val project: Project
) {
    internal constructor(
        internalState: InternalDetailedProjectState
    ) : this(
        project = internalState.project
    )
}
