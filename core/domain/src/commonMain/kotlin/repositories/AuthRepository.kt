package repositories

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(username: String, email: String, password: String): Flow<Result<Unit>>
}