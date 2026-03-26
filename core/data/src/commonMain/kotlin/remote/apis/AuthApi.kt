package remote.apis


import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import remote.apis.dtos.requests.RequestLoginBodyDto
import remote.apis.dtos.requests.RequestRegisterBodyDto
import utils.Consts

class AuthApi {
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
