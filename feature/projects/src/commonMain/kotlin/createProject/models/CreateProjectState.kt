package createProject.models

import models.ProjectRole

internal data class CreateProjectState(
    val projectId: String? = null,
    val title: String = "",
    val description: String = "",
    val industry: String = "",
    val roles: List<ProjectRole> = listOf(ProjectRole(name = "Разработчик", spots = 1)),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
