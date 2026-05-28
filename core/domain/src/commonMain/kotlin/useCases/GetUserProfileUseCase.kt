package useCases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import models.UserProfile
import repositories.UsersRepository

class GetUserProfileUseCase(
    private val usersRepository: UsersRepository,
) {
    fun current(): Flow<Result<UserProfile>> = usersRepository.getCurrentUserProfile()

    suspend fun byId(id: String): Result<UserProfile> =
        usersRepository.getUserById(id).first()
}
