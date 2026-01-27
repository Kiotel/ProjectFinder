package useCases

import repositories.AuthRepository

class SetTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String?) = authRepository.setToken(token)
}