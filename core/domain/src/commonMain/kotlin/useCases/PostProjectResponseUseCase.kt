package useCases

import repositories.ProjectsRepository

class PostProjectResponseUseCase(
    private val projectsRepository: ProjectsRepository,
) {
    suspend operator fun invoke(projectId: String, message: String): Result<Unit> =
        projectsRepository.postResponse(projectId, message)
}
