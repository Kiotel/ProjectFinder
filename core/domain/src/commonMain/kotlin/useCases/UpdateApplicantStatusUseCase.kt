package useCases

import repositories.ProjectsRepository

class UpdateApplicantStatusUseCase(private val projectsRepository: ProjectsRepository) {
    suspend operator fun invoke(responseId: String, status: String): Result<Unit> =
        projectsRepository.updateApplicantStatus(responseId, status)
}
