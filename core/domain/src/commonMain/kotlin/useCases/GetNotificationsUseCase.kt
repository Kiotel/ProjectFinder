package useCases

import repositories.NotificationsRepository

class GetNotificationsUseCase(
    private val notificationsRepository: NotificationsRepository,
) {
    suspend operator fun invoke(): Result<String> =
        notificationsRepository.getNotificationsText()
}
