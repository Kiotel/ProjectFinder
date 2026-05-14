package repositories

import io.ktor.client.call.body
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mapppers.toDomain
import models.Project
import remote.apis.BackendApi
import remote.apis.dtos.common.ResponseProjectDto
import remote.apis.dtos.responses.ResponseProjectLikeDto
import kotlin.time.Duration

internal class ProjectRepositoryImpl(
    private val backendApi: BackendApi,
    private val logger: utils.Logger
) : ProjectsRepository {
    override fun getProjects(page: Int, limit: Int, ttl: Duration): Flow<Result<List<Project>>> =
        flow {
            try {
                val apiResult = backendApi.getProjects(page = page, limit = limit)
                val fetchedProjects = apiResult.body<List<ResponseProjectDto>>()

                logger.i(
                    "ProjectsRepositoryImpl/getProjects",
                    "Fetched projects: ${fetchedProjects.map { "$it\n" }}"
                )
                emit(Result.success(fetchedProjects.map { it.toDomain() }))
            } catch (_: CancellationException) {
                return@flow
            } catch (e: Exception) {
                logger.e(
                    "ProjectsRepositoryImpl/getProjects",
                    "Error during fetch: ${e.stackTraceToString()}"
                )
            }
        }

    override fun likeProject(projectId: String): Flow<Result<Boolean>> = flow {
        try {
            val apiResult = backendApi.likeProject(projectId)
            val isLiked = apiResult.body<ResponseProjectLikeDto>().liked ?: error("API ERROR: NO LIKE")
            logger.i(
                "ProjectsRepositoryImpl/likeProject",
                "Like project $projectId: $isLiked}"
            )
            emit(Result.success(isLiked))
        } catch (e: Exception) {
            logger.e(
                "ProjectsRepositoryImpl/likeProject",
                "Error during like: ${e.stackTraceToString()}"
            )
            emit(Result.failure(e))
        }
    }

}