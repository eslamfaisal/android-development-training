package com.training.ecommerce.data.repository.user

import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface UserFirestoreRepository {
      suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>>

}