package repositories

interface NotificationsRepository {
    suspend fun getNotificationsText(): Result<String>
    suspend fun checkHealth(): Result<String>
}
