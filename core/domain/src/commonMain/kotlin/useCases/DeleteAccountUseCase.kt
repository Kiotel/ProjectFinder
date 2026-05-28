package useCases

import repositories.AuthRepository

class DeleteAccountUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.deleteAccount()
}
