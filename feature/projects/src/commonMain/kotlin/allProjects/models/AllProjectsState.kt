package allProjects.models

internal data class AllProjectsState(
    val isAuthed: Boolean,
    val searchQuery: String = "",
    val filter: ProjectFilter = ProjectFilter.ALL,
    val currentUserId: String = "",
    val participationProjectIds: List<Int> = emptyList(),
) {
    internal constructor(s: InternalAllProjectsState) : this(
        isAuthed = s.isAuthed,
        searchQuery = s.searchQuery,
        filter = s.filter,
        currentUserId = s.currentUserId,
        participationProjectIds = s.participationProjectIds,
    )
}
