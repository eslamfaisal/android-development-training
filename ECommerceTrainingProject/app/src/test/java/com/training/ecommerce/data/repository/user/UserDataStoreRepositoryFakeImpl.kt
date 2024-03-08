package com.training.ecommerce.data.repository.user

import com.training.ecommerce.data.datasource.datastore.UserPreferencesFakeDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class UserDataStoreRepositoryFakeImpl : UserPreferenceRepository {

    private val shoppingItems = MutableStateFlow(
        UserPreferencesFakeDataSource(false, "")
    )

    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        shoppingItems.value = shoppingItems.value.copy(isUserLoggedIn = isLoggedIn)
    }

    override suspend fun saveUserID(userId: String) {
        shoppingItems.value = shoppingItems.value.copy(userID = userId)
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> {
        return flow {
            emit(shoppingItems.value.isUserLoggedIn)
        }
    }

    override fun getUserID(): Flow<String?> {
        return flow {
            emit(shoppingItems.value.userID)
        }
    }

}