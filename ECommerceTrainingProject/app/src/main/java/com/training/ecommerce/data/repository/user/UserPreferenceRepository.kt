package com.training.ecommerce.data.repository.user

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    suspend fun isUserLoggedIn(): Flow<Boolean>
    suspend fun saveLoginState(isLoggedIn: Boolean)
    suspend fun saveUserID(userId: String)

}