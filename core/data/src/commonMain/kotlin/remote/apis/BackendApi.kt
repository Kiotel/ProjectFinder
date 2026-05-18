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
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import local.secureStore.AuthStore
import remote.apis.dtos.responses.ResponseRefreshTokenDto
import utils.Consts


internal class BackendApi(
    private val authStore: AuthStore, private val authApi: AuthApi
) {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
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
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(authStore.accessToken, authStore.refreshToken)
                }
                refreshTokens {
                    val newInfo = authApi.refreshAuthToken(authStore.refreshToken)
                        .body<ResponseRefreshTokenDto>()


                    authStore.setAuthData(
                        userId = newInfo.userDto?.id ?: "no user id",
                        accessToken = newInfo.accessToken ?: "no access token",
                        refreshToken = newInfo.refreshToken ?: "no refresh token",
                    )

                    BearerTokens(authStore.accessToken, authStore.refreshToken)
                }
            }

        }
    }

    suspend fun getProjects(page: Int, limit: Int) =
        client.get("${Consts.BASE_URL}/projects/") {
            contentType(ContentType.Application.Json)
            // TODO: сделать фильтрацию и сортировку
            url {
                parameters.append("page", page.toString())
                parameters.append("limit", limit.toString())
            }
        }

    suspend fun getProject(projectId: String) =
        client.get("${Consts.BASE_URL}/projects/$projectId") {
            contentType(ContentType.Application.Json)
        }

    suspend fun likeProject(projectId: String) =
        client.post("${Consts.BASE_URL}/projects/$projectId/like") {
            contentType(ContentType.Application.Json)
        }

    suspend fun getUserById(userId: String) =
        client.get("${Consts.BASE_URL}/users/$userId") {
            contentType(ContentType.Application.Json)
        }
}
