package useCases

import kotlinx.coroutines.flow.first
import models.User
import models.UserProfile
import repositories.UsersRepository

class GetUserInfoUseCase(
    private val usersRepository: UsersRepository,
) {
    suspend operator fun invoke(): Result<User> =
        usersRepository.getCurrentUserProfile().first().map { it.toUser() }
}

private fun UserProfile.toUser() = User(
    id = id,
    userName = username,
    email = email,
    fullName = displayName.takeIf { it.isNotBlank() },
    avatarUrl = avatarUrl,
)
