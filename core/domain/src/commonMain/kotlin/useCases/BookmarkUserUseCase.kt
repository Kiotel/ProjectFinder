package useCases

import repositories.UsersRepository

class BookmarkUserUseCase(
    private val usersRepository: UsersRepository,
) {
    suspend operator fun invoke(userId: String): Result<Unit> =
        usersRepository.bookmarkUser(userId)
}
