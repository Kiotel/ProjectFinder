package repositories

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    override fun getToken(): Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.access_token]?.ifBlank { null }
        }

    override suspend fun setToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.access_token] = token ?: ""
        }
    }
}

private data object PreferencesKeys {
    val access_token = stringPreferencesKey("access_token")
}