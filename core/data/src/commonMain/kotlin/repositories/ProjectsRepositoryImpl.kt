package repositories

import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import local.secureStore.AuthStore
import mapppers.toDomain
import models.Project
import models.ProjectApplicant
import models.ProjectComment
import models.ProjectMember
import models.ProjectRole
import remote.apis.BackendApi
import remote.apis.dtos.common.ProjectRoleDto
import remote.apis.dtos.common.ResponseProjectDto
import remote.apis.dtos.requests.RequestCommentBodyDto
import remote.apis.dtos.requests.RequestCreateProjectBodyDto
import remote.apis.dtos.requests.RequestProjectResponseBodyDto
import remote.apis.dtos.requests.RequestUpdateApplicantStatusDto
import remote.apis.dtos.responses.ResponseApplicantDto
import remote.apis.dtos.responses.ResponseCommentDto
import remote.apis.dtos.responses.ResponseProjectLikeDto
import remote.apis.dtos.responses.ResponseProjectsDto
import utils.Logger
import utils.httpErrorMessage
import kotlin.time.Duration

internal class ProjectsRepositoryImpl(
    private val backendApi: BackendApi,
    private val authStore: AuthStore,
    private val logger: Logger,
) : ProjectsRepository {

    override fun getProjects(page: Int, limit: Int, ttl: Duration): Flow<Result<List<Project>>> =
        flow {
            try {
                val response = backendApi.getProjects()
                if (!response.status.isSuccess()) {
                    val errorMsg = httpErrorMessage(response.status.value, " при загрузке ленты")
                    logger.e("ProjectsRepositoryImpl/getProjects", "HTTP ${response.status.value}: $errorMsg")
                    emit(Result.failure(Exception(errorMsg)))
                    return@flow
                }
                val text = response.bodyAsText()
                val all = try {
                    backendApi.json.decodeFromString<List<ResponseProjectDto>>(text).map { it.toDomain() }
                } catch (e: Exception) {
                    try {
                        val body = backendApi.json.decodeFromString<ResponseProjectsDto>(text)
                        body.projects?.map { it.toDomain() } ?: emptyList()
                    } catch (e2: Exception) {
                        logger.e("ProjectsRepositoryImpl/getProjects", "Failed to parse: ${e2.message}")
                        throw e
                    }
                }
                val start = (page - 1).coerceAtLeast(0) * limit
                val pageItems = all.drop(start).take(limit)
                logger.i(
                    "ProjectsRepositoryImpl/getProjects",
                    "Loaded ${all.size} projects, page=$page slice=${pageItems.size}",
                )
                emit(Result.success(pageItems))
            } catch (_: CancellationException) {
                return@flow
            } catch (e: Exception) {
                logger.e("ProjectsRepositoryImpl/getProjects", e.stackTraceToString())
                emit(Result.failure(e))
            }
        }

    override fun likeProject(projectId: String): Flow<Result<Boolean>> = flow {
        try {
            if (authStore.accessToken.isBlank()) {
                emit(Result.failure(Exception(httpErrorMessage(401, ""))))
                return@flow
            }
            val apiResult = backendApi.likeProject(projectId)
            if (!apiResult.status.isSuccess()) {
                val errorMsg = httpErrorMessage(apiResult.status.value, " при лайке")
                logger.e("ProjectsRepositoryImpl/likeProject", "HTTP ${apiResult.status.value}: $errorMsg")
                emit(Result.failure(Exception(errorMsg)))
                return@flow
            }
            val isLiked = apiResult.body<ResponseProjectLikeDto>().liked
                ?: error("Пустой ответ лайка")
            emit(Result.success(isLiked))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.e("ProjectsRepositoryImpl/likeProject", e.stackTraceToString())
            emit(Result.failure(e))
        }
    }

    override fun getComments(projectId: String): Flow<Result<List<ProjectComment>>> = flow {
        try {
            val response = backendApi.getProjectComments(projectId)
            if (!response.status.isSuccess()) {
                val errorMsg = httpErrorMessage(response.status.value, " при загрузке комментариев")
                logger.e("ProjectsRepositoryImpl/getComments", "HTTP ${response.status.value}: $errorMsg")
                emit(Result.failure(Exception(errorMsg)))
                return@flow
            }
            emit(Result.success(response.body<List<ResponseCommentDto>>().map { it.toDomain() }))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.e("ProjectsRepositoryImpl/getComments", e.stackTraceToString())
            emit(Result.failure(e))
        }
    }

    override suspend fun postComment(projectId: String, content: String): Result<Unit> =
        runCatching {
            if (authStore.accessToken.isBlank()) {
                error(httpErrorMessage(401, ""))
            }
            val response = backendApi.postProjectComment(
                projectId,
                RequestCommentBodyDto(content = content),
            )
            if (!response.status.isSuccess()) {
                val errorMsg = httpErrorMessage(response.status.value, " при отправке комментария")
                logger.e("ProjectsRepositoryImpl/postComment", "HTTP ${response.status.value}: $errorMsg")
                error(errorMsg)
            }
        }

    override suspend fun postResponse(projectId: String, message: String): Result<Unit> =
        runCatching {
            if (authStore.accessToken.isBlank()) {
                error(httpErrorMessage(401, ""))
            }
            val response = backendApi.postProjectResponse(
                RequestProjectResponseBodyDto(
                    projectId = projectId.toIntOrNull() ?: error("Некорректный id проекта"),
                    message = message,
                ),
            )
            if (!response.status.isSuccess()) {
                val errorMsg = httpErrorMessage(response.status.value, " при отклике")
                logger.e("ProjectsRepositoryImpl/postResponse", "HTTP ${response.status.value}: $errorMsg")
                error(errorMsg)
            }
        }

    override suspend fun getApplicants(projectId: String): Result<List<ProjectApplicant>> =
        runCatching {
            val response = backendApi.getApplicants(projectId)
            if (!response.status.isSuccess()) {
                error(httpErrorMessage(response.status.value, " при загрузке заявок"))
            }
            response.body<List<ResponseApplicantDto>>().map {
                ProjectApplicant(
                    responseId = it.responseId.toString(),
                    userId = it.userId.toString(),
                    username = it.username,
                    message = it.message,
                    status = it.status,
                )
            }
        }

    override suspend fun updateApplicantStatus(responseId: String, status: String): Result<Unit> =
        runCatching {
            val response = backendApi.updateApplicantStatus(
                responseId,
                RequestUpdateApplicantStatusDto(status),
            )
            if (!response.status.isSuccess()) {
                error(httpErrorMessage(response.status.value, " при обновлении статуса"))
            }
        }

    override suspend fun createProject(
        title: String,
        description: String?,
        industry: String?,
        roles: List<ProjectRole>,
    ): Result<Project> = runCatching {
        if (authStore.accessToken.isBlank()) {
            error(httpErrorMessage(401, ""))
        }
        val authorId = authStore.userId.toIntOrNull()
            ?: error("Не удалось определить id автора")
        val json = Json { encodeDefaults = false }
        val response = backendApi.createProject(
            RequestCreateProjectBodyDto(
                authorId = authorId,
                title = title,
                description = description,
                industry = industry,
                status = "idea",
                roles = roles.map { role ->
                    ProjectRoleDto(
                        roleName = role.name,
                        spotsTotal = role.spots,
                        requiredSkills = if (role.skills.isNotEmpty()) json.encodeToString(role.skills) else null,
                    )
                },
            ),
        )
        if (!response.status.isSuccess()) {
            val errorMsg = httpErrorMessage(response.status.value, " при создании проекта")
            logger.e("ProjectsRepositoryImpl/createProject", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
        response.body<ResponseProjectDto>().toDomain()
    }

    override suspend fun updateProject(
        projectId: String,
        title: String,
        description: String?,
        industry: String?,
        roles: List<ProjectRole>,
    ): Result<Unit> = runCatching {
        if (authStore.accessToken.isBlank()) {
            error(httpErrorMessage(401, ""))
        }
        val authorId = authStore.userId.toIntOrNull()
            ?: error("Не удалось определить id автора")
        val json = Json { encodeDefaults = false }
        val response = backendApi.updateProject(
            projectId,
            RequestCreateProjectBodyDto(
                authorId = authorId,
                title = title,
                description = description,
                industry = industry,
                roles = roles.map { role ->
                    ProjectRoleDto(
                        roleName = role.name,
                        spotsTotal = role.spots,
                        requiredSkills = if (role.skills.isNotEmpty()) json.encodeToString(role.skills) else "[]",
                    )
                },
            ),
        )
        if (!response.status.isSuccess()) {
            val errorMsg = httpErrorMessage(response.status.value, " при обновлении проекта")
            logger.e("ProjectsRepositoryImpl/updateProject", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
    }

    override suspend fun deleteProject(projectId: String): Result<Unit> = runCatching {
        if (authStore.accessToken.isBlank()) {
            error(httpErrorMessage(401, ""))
        }
        val response = backendApi.deleteProject(projectId)
        if (!response.status.isSuccess()) {
            val errorMsg = httpErrorMessage(response.status.value, " при удалении проекта")
            logger.e("ProjectsRepositoryImpl/deleteProject", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
    }

    private fun ResponseCommentDto.toDomain() = ProjectComment(
        id = id?.toString() ?: "0",
        projectId = projectId?.toString() ?: "0",
        authorId = (userId ?: authorId)?.toString(),
        authorName = authorName,
        content = content.orEmpty(),
        createdAt = createdAt,
    )

    override suspend fun getMyParticipationProjects(): Result<List<Int>> = runCatching {
        if (authStore.accessToken.isBlank()) {
            error(httpErrorMessage(401, ""))
        }
        val response = backendApi.getMyParticipationProjects()
        if (!response.status.isSuccess()) {
            val errorMsg = httpErrorMessage(response.status.value, " при загрузке проектов участия")
            logger.e("ProjectsRepositoryImpl/getMyParticipationProjects", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
        val text = response.bodyAsText()
        val map = backendApi.json.decodeFromString<Map<String, List<Int>>>(text)
        map["projectIds"] ?: emptyList()
    }

    override suspend fun getProjectMembers(projectId: String): Result<List<ProjectMember>> = runCatching {
        val response = backendApi.getProjectMembers(projectId)
        if (!response.status.isSuccess()) {
            val errorMsg = httpErrorMessage(response.status.value, " при загрузке участников")
            logger.e("ProjectsRepositoryImpl/getProjectMembers", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
        response.body<List<ProjectMember>>()
    }
}
