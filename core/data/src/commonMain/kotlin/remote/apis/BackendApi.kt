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
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import local.secureStore.TokenStore
import remote.apis.dtos.requests.RequestLoginBodyDto
import remote.apis.dtos.requests.RequestRefreshAuthTokenBodyDto
import remote.apis.dtos.requests.RequestRegisterBodyDto
import remote.apis.dtos.responses.ResponseRefreshTokenDto
import utils.Consts


class BackendApi(
    private val tokenStore: TokenStore
) {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        allowTrailingComma = true
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

                    BearerTokens(tokenStore.accessToken, tokenStore.refreshToken)
                }
                refreshTokens {
                    val newInfo =
                        refreshAuthToken(tokenStore.refreshToken).body<ResponseRefreshTokenDto>()


                    tokenStore.setTokens(
                        accessToken = newInfo.accessToken,
                        refreshToken = newInfo.refreshToken
                    )

                    BearerTokens(tokenStore.accessToken, tokenStore.refreshToken)
                }
            }

        }
    }


    suspend fun refreshAuthToken(refreshToken: String): HttpResponse {
        return client.post("${Consts.BASE_URL}/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(
                RequestRefreshAuthTokenBodyDto(
                    refreshToken = refreshToken
                )
            )
        }
    }

    suspend fun register(username: String, email: String, password: String) =
        client.post("${Consts.BASE_URL}/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(
                RequestRegisterBodyDto(
                    username = username,
                    email = email,
                    password = password,
                )
            )
        }

    suspend fun login(email: String, password: String) =
        client.post("${Consts.BASE_URL}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                RequestLoginBodyDto(
                    email = email,
                    password = password
                )
            )
        }
}
