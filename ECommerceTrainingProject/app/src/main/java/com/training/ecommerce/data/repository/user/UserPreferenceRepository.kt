package com.training.ecommerce.data.repository.user

import com.training.ecommerce.data.models.user.UserDetailsPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    suspend fun getUserDetails(): Flow<UserDetailsPreferences>
    suspend fun updateUserId(userId: String)
    suspend fun clearUserPreferences()
}