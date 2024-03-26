package com.training.ecommerce.data.repository.user

import com.training.ecommerce.ui.common.models.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface UserFirestoreRepository {
    suspend fun getUserDetails(userId: String): Flow<UserDetailsModel>

}