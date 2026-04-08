package repositories

import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import local.database.ProjectsDataBase
import mapppers.toDomain
import mapppers.toEntity
import models.Project
import remote.apis.BackendApi
import remote.apis.dtos.common.ProjectDto
import kotlin.time.Clock.System.now
import kotlin.time.Duration

internal class ProjectRepositoryImpl(
    private val projectsDataBase: ProjectsDataBase,
    private val backendApi: BackendApi,
    private val logger: utils.Logger
) : ProjectsRepository {
    override fun getProjects(page: Int, limit: Int, ttl: Duration): Flow<Result<List<Project>>> =
        flow {
            val offset = (page - 1) * limit
            val localEntities = projectsDataBase.projectsDao().getPaged(offset, limit)
            logger.i(
                "ProjectsRepositoryImpl/getProjects",
                "Got local projects: ${localEntities.map { it.title }}"
            )

            if (localEntities.isNotEmpty()) {
                emit(Result.success(localEntities.map { it.toDomain() }))
            }

            val isCacheStale = localEntities.isEmpty() || localEntities.any {
                now().toEpochMilliseconds() - it.lastFetched > ttl.inWholeMilliseconds
            }

            if (isCacheStale) {
                logger.i("ProjectsRepositoryImpl/getProjects", "Cache is stale.")
                try {
                    val apiResult = backendApi.getProjects(page = page, limit = limit)
                    val fetchedProjects = apiResult.body<List<ProjectDto>>()

                    fetchedProjects.forEach { projectsDataBase.projectsDao().upsert(it.toEntity()) }
                    logger.i(
                        "ProjectsRepositoryImpl/getProjects",
                        "Fetched projects: ${fetchedProjects.map { "$it\n" }}"
                    )
                    emit(Result.success(fetchedProjects.map { it.toDomain() }))
                } catch (e: Exception) {
                    logger.e(
                        "ProjectsRepositoryImpl/getProjects",
                        "Error during fetch: ${e.stackTraceToString()}"
                    )
                    if (localEntities.isEmpty()) {
                        emit(Result.failure(Exception("Network error and no cache available. Exception: ${e.stackTraceToString()}")))
                    }
                }
            }
        }
}