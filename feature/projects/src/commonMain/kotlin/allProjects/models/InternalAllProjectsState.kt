package allProjects.models

internal enum class ProjectFilter { ALL, MINE, JOINED }

internal data class InternalAllProjectsState(
    val isAuthed: Boolean = false,
    val searchQuery: String = "",
    val filter: ProjectFilter = ProjectFilter.ALL,
    val currentUserId: String = "",
    val participationProjectIds: List<Int> = emptyList(),
)
