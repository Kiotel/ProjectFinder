package apis

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.delay

class TestApi {
    private val client = HttpClient()

    suspend fun test(): String {
        val response = client.get("https://ktor.io/docs/")
        delay(5000)
        return response.bodyAsText()
    }
}