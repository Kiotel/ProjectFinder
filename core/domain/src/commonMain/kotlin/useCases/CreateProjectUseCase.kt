package useCases

import models.Project
import repositories.ProjectsRepository

class CreateProjectUseCase(
    private val projectsRepository: ProjectsRepository,
) {
    suspend operator fun invoke(
        title: String,
        description: String?,
        industry: String? = null,
        roles: List<Pair<String, Int>> = emptyList(),
    ): Result<Project> = projectsRepository.createProject(title, description, industry, roles)
}
