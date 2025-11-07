package com.example.gestior.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.gestior.data.remote.TokenProvider
import com.example.gestior.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenProvider {

    companion object {
        val AUTH_TOKEN = stringPreferencesKey(Constants.KEY_AUTH_TOKEN)
        val USER_ID = intPreferencesKey(Constants.KEY_USER_ID)
        val USER_EMAIL = stringPreferencesKey(Constants.KEY_USER_EMAIL)
        val USER_NAME = stringPreferencesKey(Constants.KEY_USER_NAME)
        val IS_LOGGED_IN = booleanPreferencesKey(Constants.KEY_IS_LOGGED_IN)
    }

    // Token management
    override suspend fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN]
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
            preferences[IS_LOGGED_IN] = false
        }
    }

    // User data management
    suspend fun saveUserData(userId: Int, email: String, name: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = name
        }
    }

    fun getUserId(): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }

    fun getUserEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }
    }

    fun getUserName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_NAME]
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }

    // Clear all data
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
