package useCases

import models.UserProfile
import repositories.UsersRepository

class UpdateUserProfileUseCase(
    private val usersRepository: UsersRepository,
) {
    suspend operator fun invoke(profile: UserProfile): Result<Unit> =
        usersRepository.updateProfile(profile)
}
