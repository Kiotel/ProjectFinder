package useCases

import models.Project
import models.ProjectRole
import repositories.ProjectsRepository

class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository,
) {
    suspend operator fun invoke(
        title: String,
        description: String?,
        industry: String? = null,
        roles: List<ProjectRole> = emptyList(),
    ): Result<Project> = projectsRepository.createProject(title, description, industry, roles)
}
