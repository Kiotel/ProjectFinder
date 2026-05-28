package useCases

import kotlinx.coroutines.flow.first
import models.PagedUsers
import repositories.UsersRepository

class SearchUsersUseCase(
    private val usersRepository: UsersRepository,
) {
    suspend operator fun invoke(
        query: String = "",
        page: Int = 1,
        limit: Int = 20,
    ): Result<PagedUsers> = usersRepository.searchUsers(query, page, limit).first()
}
