package repositories

import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mapppers.toDomain
import models.Project
import remote.apis.BackendApi
import remote.apis.dtos.common.ProjectDto
import kotlin.time.Duration

internal class ProjectRepositoryImpl(
    private val backendApi: BackendApi,
    private val logger: utils.Logger
) : ProjectsRepository {
    override fun getProjects(page: Int, limit: Int, ttl: Duration): Flow<Result<List<Project>>> =
        flow {
            try {
                val apiResult = backendApi.getProjects(page = page, limit = limit)
                val fetchedProjects = apiResult.body<List<ProjectDto>>()

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
            }
        }

}