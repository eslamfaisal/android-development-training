package com.training.ecommerce.data.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesDataSource(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.IS_USER_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun saveUserID(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.USER_ID] = userId
        }
    }

    val isUserLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DataStoreKeys.IS_USER_LOGGED_IN] ?: false
        }

    val userID: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[DataStoreKeys.USER_ID]
        }
}
