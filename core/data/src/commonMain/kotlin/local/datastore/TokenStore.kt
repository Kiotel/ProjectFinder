package local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

class TokenStore(
    private val dataStore: DataStore<Preferences>
) {
    fun getAccessToken(): Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.access_token]?.ifBlank { null }
        }

    suspend fun setAccessToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.access_token] = token ?: ""
        }
    }

    fun getRefreshToken(): Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.refresh_token]?.ifBlank { null }
        }

    suspend fun setRefreshToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.refresh_token] = token ?: ""
        }
    }
}

private data object PreferencesKeys {
    val access_token = stringPreferencesKey("access_token")
    val refresh_token = stringPreferencesKey("refresh_token")
}