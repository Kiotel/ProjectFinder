package useCases

import kotlinx.coroutines.flow.Flow
import models.User
import repositories.AuthRepository

class GetUserInfoUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> =
        authRepository.getUserInfo(
            cacheTtl = 1000 * 60 * 60 * 24 // 24 часа
        )
}