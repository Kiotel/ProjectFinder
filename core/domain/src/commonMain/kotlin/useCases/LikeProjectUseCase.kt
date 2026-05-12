package useCases

import kotlinx.coroutines.flow.Flow
import repositories.ProjectsRepository

class LikeProjectUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(projectId: String): Flow<Result<Boolean>> =
        projectsRepository.likeProject(projectId)
}