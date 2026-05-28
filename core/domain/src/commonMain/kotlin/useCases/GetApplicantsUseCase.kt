package useCases

import models.ProjectApplicant
import repositories.ProjectsRepository

class GetApplicantsUseCase(private val projectsRepository: ProjectsRepository) {
    suspend operator fun invoke(projectId: String): Result<List<ProjectApplicant>> =
        projectsRepository.getApplicants(projectId)
}
