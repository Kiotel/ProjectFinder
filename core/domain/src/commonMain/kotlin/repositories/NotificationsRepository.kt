package repositories

import models.Notification

interface NotificationsRepository {
    suspend fun getNotifications(): Result<List<Notification>>
    suspend fun checkHealth(): Result<String>
}
