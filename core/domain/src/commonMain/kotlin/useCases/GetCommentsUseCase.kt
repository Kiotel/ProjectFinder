package useCases

import kotlinx.coroutines.flow.first
import models.ProjectComment
import repositories.ProjectsRepository

class GetCommentsUseCase(
    private val projectsRepository: ProjectsRepository,
) {
    suspend operator fun invoke(projectId: String): Result<List<ProjectComment>> =
        projectsRepository.getComments(projectId).first()
}
