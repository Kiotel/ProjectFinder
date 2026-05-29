package useCases

import models.ProjectRole
import repositories.ProjectsRepository

class UpdateProjectUseCase(
    private val repository: ProjectsRepository
) {
    suspend operator fun invoke(
        projectId: String,
        title: String,
        description: String?,
        industry: String?,
        roles: List<ProjectRole>,
    ): Result<Unit> {
        return repository.updateProject(projectId, title, description, industry, roles)
    }
}
