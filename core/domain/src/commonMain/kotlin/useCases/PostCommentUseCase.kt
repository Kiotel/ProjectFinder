package useCases

import repositories.ProjectsRepository

class PostCommentUseCase(
    private val projectsRepository: ProjectsRepository,
) {
    suspend operator fun invoke(projectId: String, content: String): Result<Unit> =
        projectsRepository.postComment(projectId, content)
}
