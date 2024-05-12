package com.training.ecommerce.data.repository.common

import com.training.ecommerce.data.datasource.datastore.AppPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppDataStoreRepositoryImpl @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : AppPreferenceRepository {

    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        appPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun isLoggedIn(): Flow<Boolean> {
        return appPreferencesDataSource.isUserLoggedIn
    }
}
