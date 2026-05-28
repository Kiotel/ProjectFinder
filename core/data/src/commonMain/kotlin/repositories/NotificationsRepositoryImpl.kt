package repositories

import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import remote.apis.BackendApi
import utils.Logger
import utils.endpointUnavailableMessage
import utils.httpErrorMessage

internal class NotificationsRepositoryImpl(
    private val backendApi: BackendApi,
    private val logger: Logger,
) : NotificationsRepository {

    override suspend fun getNotificationsText(): Result<String> = runCatching {
        val response = backendApi.getNotifications()
        if (!response.status.isSuccess()) {
            val errorMsg = when (response.status.value) {
                404 -> endpointUnavailableMessage("Уведомления")
                else -> httpErrorMessage(response.status.value, " при загрузке уведомлений")
            }
            logger.e("NotificationsRepositoryImpl/getNotificationsText", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
        response.bodyAsText().ifBlank { "Нет уведомлений" }
    }

    override suspend fun checkHealth(): Result<String> = runCatching {
        val response = backendApi.health()
        if (!response.status.isSuccess()) {
            val errorMsg = "Сервер недоступен: ${response.status.value}"
            logger.e("NotificationsRepositoryImpl/checkHealth", errorMsg)
            error(errorMsg)
        }
        response.bodyAsText().ifBlank { "OK" }
    }
}
