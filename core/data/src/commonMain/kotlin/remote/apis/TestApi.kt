package remote.apis

import remote.apis.dtos.KtorTextDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.delay

class TestApi {
    private val client = HttpClient()

    suspend fun test(): KtorTextDto {
        val response = client.get("https://ktor.io/docs/")
        delay(5000)
        val result = KtorTextDto(response.bodyAsText())
        return result
    }
}