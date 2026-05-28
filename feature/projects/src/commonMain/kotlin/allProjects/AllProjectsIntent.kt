package allProjects

import allProjects.models.ProjectFilter

internal sealed interface AllProjectsIntent {
    object GoToCreateProject : AllProjectsIntent
    data class UpdateSearchQuery(val query: String) : AllProjectsIntent
    data class SetFilter(val filter: ProjectFilter) : AllProjectsIntent
}
