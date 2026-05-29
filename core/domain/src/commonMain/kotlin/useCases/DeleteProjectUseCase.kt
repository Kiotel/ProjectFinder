package useCases

import repositories.ProjectsRepository

class DeleteProjectUseCase(
    private val repository: ProjectsRepository
) {
    suspend operator fun invoke(projectId: String): Result<Unit> {
        return repository.deleteProject(projectId)
    }
}
