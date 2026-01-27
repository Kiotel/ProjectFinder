package useCases

import kotlinx.coroutines.flow.Flow
import repositories.AuthRepository

class GetTokenUseCase(
    private val preferencesRepository: AuthRepository
) {
    operator fun invoke(): Flow<String?> = preferencesRepository.getToken()
}