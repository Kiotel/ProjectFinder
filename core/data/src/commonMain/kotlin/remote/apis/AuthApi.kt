package remote.apis


import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
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
import utils.Consts


internal class AuthApi(
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
            logger = io.ktor.client.plugins.logging.Logger.DEFAULT
            level = LogLevel.ALL
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
