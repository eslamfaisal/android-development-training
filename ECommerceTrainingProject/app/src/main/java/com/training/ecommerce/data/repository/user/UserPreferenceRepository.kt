package com.training.ecommerce.data.repository.user

import com.training.ecommerce.data.models.user.UserDetailsPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    fun getUserDetails(): Flow<UserDetailsPreferences>
    suspend fun updateUserId(userId: String)
    fun getUSerId(): Flow<String>
    suspend fun clearUserPreferences()
    suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences)
}