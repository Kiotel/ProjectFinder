package repositories

import kotlinx.coroutines.flow.Flow
import models.User

interface UsersRepository {
    fun getCurrentUserInfo(): Flow<Result<User>>
    fun getUserById(id: String): Flow<Result<User>>
    fun getUsers(): Flow<Result<List<User>>>
}