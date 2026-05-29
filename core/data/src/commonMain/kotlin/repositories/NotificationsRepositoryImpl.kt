package repositories

import io.ktor.client.call.body
import io.ktor.http.isSuccess
import models.Notification
import models.NotificationType
import remote.apis.BackendApi
import remote.apis.dtos.responses.ResponseNotificationDto
import utils.Logger
import utils.endpointUnavailableMessage
import utils.httpErrorMessage

internal class NotificationsRepositoryImpl(
    private val backendApi: BackendApi,
    private val logger: Logger,
) : NotificationsRepository {

    override suspend fun getNotifications(): Result<List<Notification>> = runCatching {
        val response = backendApi.getNotifications()
        if (!response.status.isSuccess()) {
            val errorMsg = when (response.status.value) {
                404 -> endpointUnavailableMessage("Уведомления")
                else -> httpErrorMessage(response.status.value, " при загрузке уведомлений")
            }
            logger.e("NotificationsRepositoryImpl/getNotifications", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
        val dtos: List<ResponseNotificationDto> = response.body()
        dtos.map { it.toDomain() }
    }

    override suspend fun checkHealth(): Result<String> = runCatching {
        val response = backendApi.health()
        if (!response.status.isSuccess()) {
            val errorMsg = "Сервер недоступен: ${response.status.value}"
            logger.e("NotificationsRepositoryImpl/checkHealth", errorMsg)
            error(errorMsg)
        }
        response.body<Unit>()
        "OK"
    }

    private fun ResponseNotificationDto.toDomain() = Notification(
        id = id ?: 0,
        title = title ?: "Без темы",
        content = content ?: "",
        type = parseType(type),
        isRead = isRead ?: false,
        createdAt = createdAt,
    )

    private fun parseType(type: String?): NotificationType = when (type?.uppercase()) {
        "LIKE" -> NotificationType.LIKE
        "BOOKMARK" -> NotificationType.BOOKMARK
        "RESPONSE" -> NotificationType.RESPONSE
        "MESSAGE" -> NotificationType.MESSAGE
        else -> NotificationType.OTHER
    }
}
