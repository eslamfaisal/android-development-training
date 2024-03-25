package com.training.ecommerce.data.repository.user

import com.training.ecommerce.data.datasource.datastore.UserPreferencesFakeDataSource
import com.training.ecommerce.data.repository.common.AppPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class AppDataStoreRepositoryFakeImpl : AppPreferenceRepository {

    private val shoppingItems = MutableStateFlow(
        UserPreferencesFakeDataSource(false, "")
    )

    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        shoppingItems.value = shoppingItems.value.copy(isUserLoggedIn = isLoggedIn)
    }

    override suspend fun isLoggedIn(): Flow<Boolean> {
        return flow {
            emit(shoppingItems.value.isUserLoggedIn)
        }
    }

}