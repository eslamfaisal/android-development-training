package com.training.ecommerce.data.repository.user

import android.content.Context
import com.training.ecommerce.data.datasource.datastore.userDetailsDataStore
import com.training.ecommerce.data.models.user.UserDetailsPreferences
import kotlinx.coroutines.flow.Flow

class UserPreferenceRepositoryImpl(private val context: Context) : UserPreferenceRepository {
    override suspend fun getUserDetails(): Flow<UserDetailsPreferences> {
        return context.userDetailsDataStore.data
    }

    override suspend fun updateUserId(userId: String) {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().setId(userId).build()
        }
    }

    override suspend fun clearUserPreferences() {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().clear().build()
        }
    }

    override suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences) {
        context.userDetailsDataStore.updateData { userDetailsPreferences }
    }
}