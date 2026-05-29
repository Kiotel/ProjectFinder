package models

data class Notification(
    val id: Int,
    val title: String,
    val content: String,
    val type: NotificationType,
    val isRead: Boolean,
    val createdAt: String?,
)

enum class NotificationType(val displayName: String) {
    LIKE("Лайк"),
    BOOKMARK("Закладка"),
    RESPONSE("Отклик"),
    MESSAGE("Сообщение"),
    OTHER("Уведомление"),
}
