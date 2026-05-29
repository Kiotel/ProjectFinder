package useCases

import repositories.ProjectsRepository

class GetMyParticipationProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {
    suspend operator fun invoke(): Result<List<Int>> = projectsRepository.getMyParticipationProjects()
}
