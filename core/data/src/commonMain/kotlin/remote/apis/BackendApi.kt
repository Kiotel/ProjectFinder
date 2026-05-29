package remote.apis

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import local.secureStore.AuthStore
import remote.apis.dtos.requests.RequestCommentBodyDto
import remote.apis.dtos.requests.RequestCreateProjectBodyDto
import remote.apis.dtos.requests.RequestProjectResponseBodyDto
import remote.apis.dtos.requests.RequestUpdateUserBodyDto
import remote.apis.dtos.responses.ResponseRefreshTokenDto
import utils.Consts

internal class BackendApi(
    private val authStore: AuthStore,
    private val authApi: AuthApi,
) {
    @OptIn(ExperimentalSerializationApi::class)
    internal val json = Json {
        allowTrailingComma = true
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL // Changed from INFO to ALL to see request bodies
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val access = authStore.accessToken
                    if (access.isBlank()) null
                    else BearerTokens(access, authStore.refreshToken)
                }
                refreshTokens {
                    val newInfo = authApi.refreshAuthToken(authStore.refreshToken)
                        .body<ResponseRefreshTokenDto>()
                    authStore.setAuthData(
                        userId = newInfo.userDto?.id?.toString() ?: authStore.userId,
                        accessToken = newInfo.accessToken ?: authStore.accessToken,
                        refreshToken = newInfo.refreshToken ?: authStore.refreshToken,
                    )
                    BearerTokens(authStore.accessToken, authStore.refreshToken)
                }
            }
        }
    }

    suspend fun health() = client.get("${Consts.BASE_URL}/health")

    suspend fun getNotifications() = client.get("${Consts.BASE_URL}/notifications") {
        contentType(ContentType.Application.Json)
    }

    /** Сервер отдаёт полный список без пагинации. */
    suspend fun getProjects() =
        client.get("${Consts.BASE_URL}/projects/") {
            contentType(ContentType.Application.Json)
        }

    suspend fun createProject(body: RequestCreateProjectBodyDto) =
        client.post("${Consts.BASE_URL}/projects/") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun updateProject(projectId: String, body: RequestCreateProjectBodyDto) =
        client.put("${Consts.BASE_URL}/projects/$projectId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun deleteProject(projectId: String) =
        client.delete("${Consts.BASE_URL}/projects/$projectId") {
            contentType(ContentType.Application.Json)
        }

    suspend fun likeProject(projectId: String) =
        client.post("${Consts.BASE_URL}/projects/$projectId/like") {
            contentType(ContentType.Application.Json)
            setBody("{}")
        }

    suspend fun getProjectComments(projectId: String) =
        client.get("${Consts.BASE_URL}/projects/$projectId/comments") {
            contentType(ContentType.Application.Json)
        }

    suspend fun postProjectComment(projectId: String, body: RequestCommentBodyDto) =
        client.post("${Consts.BASE_URL}/projects/$projectId/comments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun postProjectResponse(body: RequestProjectResponseBodyDto) =
        client.post("${Consts.BASE_URL}/responses") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun getApplicants(projectId: String) =
        client.get("${Consts.BASE_URL}/responses/project/$projectId") {
            contentType(ContentType.Application.Json)
        }

    suspend fun getProjectMembers(projectId: String) =
        client.get("${Consts.BASE_URL}/responses/project/$projectId/members") {
            contentType(ContentType.Application.Json)
        }

    suspend fun getMyParticipationProjects() =
        client.get("${Consts.BASE_URL}/responses/my-projects") {
            contentType(ContentType.Application.Json)
        }

    suspend fun updateApplicantStatus(responseId: String, body: remote.apis.dtos.requests.RequestUpdateApplicantStatusDto) =
        client.patch("${Consts.BASE_URL}/responses/$responseId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun searchUsers(query: String? = null, page: Int = 1, limit: Int = 20) =
        client.get("${Consts.BASE_URL}/users/search") {
            contentType(ContentType.Application.Json)
            url {
                query?.takeIf { it.isNotBlank() }?.let { parameters.append("query", it) }
                parameters.append("page", page.toString())
                parameters.append("limit", limit.toString())
            }
        }

    suspend fun getUserById(userId: String) =
        client.get("${Consts.BASE_URL}/users/$userId") {
            contentType(ContentType.Application.Json)
        }

    suspend fun updateUser(userId: String, body: RequestUpdateUserBodyDto) =
        client.put("${Consts.BASE_URL}/users/$userId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun bookmarkUser(userId: String) =
        client.post("${Consts.BASE_URL}/users/$userId/bookmark") {
            contentType(ContentType.Application.Json)
            setBody("{}")
        }

    suspend fun uploadFile(bytes: ByteArray, fileName: String) =
        client.submitFormWithBinaryData(
            url = "${Consts.BASE_URL}/uploads",
            formData = formData {
                append(
                    key = "f",
                    value = bytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "application/octet-stream")
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    },
                )
            },
        )
}
