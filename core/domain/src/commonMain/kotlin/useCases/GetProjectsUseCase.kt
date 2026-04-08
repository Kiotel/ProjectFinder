package useCases

import kotlinx.coroutines.flow.Flow
import models.Project
import repositories.ProjectsRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class GetProjectsUseCase(
    private val projectsRepository: ProjectsRepository
) {
    operator fun invoke(
        page: Int,
        limit: Int,
        ttl: Duration = 10.seconds
    ): Flow<Result<List<Project>>> =
        projectsRepository.getProjects(
            page = page,
            limit = limit,
            ttl = ttl
        )
}