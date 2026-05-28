package repositories

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(username: String, email: String, password: String): Flow<Result<Unit>>
    fun login(email: String, password: String): Flow<Result<Unit>>
    fun isAuthed(): Flow<Result<Unit>>
    suspend fun logOut()
    suspend fun deleteAccount(): Result<Unit>
}