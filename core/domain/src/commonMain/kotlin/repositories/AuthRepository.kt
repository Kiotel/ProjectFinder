package repositories

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getToken(): Flow<String?>
    suspend fun setToken(token: String?)
}