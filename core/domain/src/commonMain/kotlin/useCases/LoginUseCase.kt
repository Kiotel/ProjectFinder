package useCases

import kotlinx.coroutines.flow.Flow
import repositories.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Result<Unit>> = authRepository.login(
        email = email, password = password
    )
}