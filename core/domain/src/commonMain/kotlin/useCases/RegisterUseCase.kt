package useCases

import kotlinx.coroutines.flow.Flow
import repositories.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(username: String, email: String, password: String): Flow<Result<Unit>> =
        authRepository.register(
            username = username,
            email = email,
            password = password
        )
}