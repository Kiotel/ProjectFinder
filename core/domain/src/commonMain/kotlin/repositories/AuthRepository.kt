package repositories

import kotlinx.coroutines.flow.Flow
import models.User

interface AuthRepository {
    fun register(username: String, email: String, password: String): Flow<Result<Unit>>
    fun login(email: String, password: String): Flow<Result<Unit>>
    suspend fun getUserInfo(cacheTtl: Long): Result<User>

    fun isAuthed(): Flow<Result<Unit>>
    suspend fun logOut()
}