package remote.apis


import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import remote.apis.dtos.requests.RequestLoginBodyDto
import remote.apis.dtos.requests.RequestRefreshAuthTokenBodyDto
import remote.apis.dtos.requests.RequestRegisterBodyDto
import utils.Consts

@Serializable
data class AuthTokens(
    val accessToken: String = "",
    val refreshToken: String = ""
)

class AuthApi(
    private val kSafe: KSafe
) {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        allowTrailingComma = true
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val client = HttpClient {
        var tokens by kSafe(AuthTokens())

        install(ContentNegotiation) {
            json(json)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(tokens.accessToken, tokens.refreshToken)
                }
                refreshTokens {
                    val newInfo = refreshAuthToken(tokens.refreshToken)

                    tokens = AuthTokens(
                        accessToken = newInfo.accessToken,
                        refreshToken = newInfo.refreshToken
                    )

                    BearerTokens(tokens.accessToken, tokens.refreshToken)
                }
            }
        }
    }


    suspend fun refreshAuthToken(refreshToken: String) =
        client.post("${Consts.BASE_URL}/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(
                RequestRefreshAuthTokenBodyDto(
                    refreshToken = refreshToken
                )
            )
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
