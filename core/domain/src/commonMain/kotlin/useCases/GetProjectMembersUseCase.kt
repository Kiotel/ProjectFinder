package useCases

import models.ProjectMember
import repositories.ProjectsRepository

class GetProjectMembersUseCase(
    private val projectsRepository: ProjectsRepository
) {
    suspend operator fun invoke(projectId: String): Result<List<ProjectMember>> = projectsRepository.getProjectMembers(projectId)
}
