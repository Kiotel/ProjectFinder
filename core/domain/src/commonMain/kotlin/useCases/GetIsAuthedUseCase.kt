package useCases

import kotlinx.coroutines.flow.Flow
import repositories.AuthRepository

class GetIsAuthedUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Result<Unit>> =
        authRepository.isAuthed()
}