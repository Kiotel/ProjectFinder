package repositories

import kotlinx.coroutines.flow.Flow
import models.PagedUsers
import models.UserProfile

interface UsersRepository {
    fun getCurrentUserProfile(): Flow<Result<UserProfile>>
    fun getUserById(id: String): Flow<Result<UserProfile>>
    fun searchUsers(query: String, page: Int, limit: Int): Flow<Result<PagedUsers>>
    suspend fun updateProfile(profile: UserProfile): Result<Unit>
    suspend fun bookmarkUser(userId: String): Result<Unit>
}
