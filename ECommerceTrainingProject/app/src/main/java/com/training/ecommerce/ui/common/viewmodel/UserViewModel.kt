package com.training.ecommerce.ui.common.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.training.ecommerce.data.datasource.datastore.AppPreferencesDataSource
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.user.UserDetailsPreferences
import com.training.ecommerce.data.repository.common.AppDataStoreRepositoryImpl
import com.training.ecommerce.data.repository.common.AppPreferenceRepository
import com.training.ecommerce.data.repository.user.UserFirestoreRepository
import com.training.ecommerce.data.repository.user.UserFirestoreRepositoryImpl
import com.training.ecommerce.data.repository.user.UserPreferenceRepository
import com.training.ecommerce.data.repository.user.UserPreferenceRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(
    private val appPreferencesRepository: AppPreferenceRepository,
    private val userPreferencesRepository: UserPreferenceRepository,
    private val userFirestoreRepository: UserFirestoreRepository
) : ViewModel() {

    init {
        listenToUserDetails()
    }

    private fun listenToUserDetails() = viewModelScope.launch {
        userFirestoreRepository.getUserDetails(
            userPreferencesRepository.getUserDetails().first().id
        ).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        UserDetailsPreferences.newBuilder().setId(it.id).setEmail(it.email).build()
                            .let { userDetails ->
                                userPreferencesRepository.updateUserDetails(userDetails)
                            }
                    }
                }

                else -> {
                    // Do nothing
                }
            }
        }
    }

    suspend fun isUserLoggedIn() = appPreferencesRepository.isLoggedIn()
    suspend fun logOut() = viewModelScope.launch {
        userPreferencesRepository.clearUserPreferences()
        appPreferencesRepository.saveLoginState(false)
    }
}

class UserViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    private val appPreferencesRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(context))
    private val userPreferencesRepository = UserPreferenceRepositoryImpl(context)
    private val userFirestoreRepository = UserFirestoreRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserViewModel(
                appPreferencesRepository, userPreferencesRepository, userFirestoreRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}