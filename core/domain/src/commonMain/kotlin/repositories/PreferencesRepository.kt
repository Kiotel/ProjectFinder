package repositories

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getIsEverLogged(): Flow<Boolean>
    suspend fun setIsEverLogged(isEverLogged: Boolean)
    fun getFilledDescription(): Flow<Boolean>
    suspend fun setFilledDescription(isFilled: Boolean)
}