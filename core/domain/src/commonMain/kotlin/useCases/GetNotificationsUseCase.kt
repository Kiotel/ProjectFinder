package useCases

import models.Notification
import repositories.NotificationsRepository

class GetNotificationsUseCase(
    private val notificationsRepository: NotificationsRepository,
) {
    suspend operator fun invoke(): Result<List<Notification>> =
        notificationsRepository.getNotifications()
}
