package repositories

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {
    override fun getIsEverLogged(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.isEverLogged] ?: false
        }

    override suspend fun setIsEverLogged(isEverLogged: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.isEverLogged] = isEverLogged
        }
    }

    override fun getFilledDescription(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.isFilledDescriptionForm] ?: false
        }

    override suspend fun setFilledDescription(isFilled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.isFilledDescriptionForm] = isFilled
        }
    }
}

private data object PreferencesKeys {
    val isFilledDescriptionForm = booleanPreferencesKey("is_filled_description_form")
    val isEverLogged = booleanPreferencesKey("is_ever_logged")
}