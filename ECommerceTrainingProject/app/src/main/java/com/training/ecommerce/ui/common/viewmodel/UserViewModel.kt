package com.training.ecommerce.ui.common.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.training.ecommerce.data.datasource.datastore.AppPreferencesDataSource
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.training.ecommerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.training.ecommerce.data.repository.common.AppDataStoreRepositoryImpl
import com.training.ecommerce.data.repository.common.AppPreferenceRepository
import com.training.ecommerce.data.repository.user.UserFirestoreRepository
import com.training.ecommerce.data.repository.user.UserFirestoreRepositoryImpl
import com.training.ecommerce.data.repository.user.UserPreferenceRepository
import com.training.ecommerce.data.repository.user.UserPreferenceRepositoryImpl
import com.training.ecommerce.domain.models.toUserDetailsModel
import com.training.ecommerce.domain.models.toUserDetailsPreferences
import com.training.ecommerce.utils.CrashlyticsUtils
import com.training.ecommerce.utils.CrashlyticsUtils.LISTEN_TO_USER_DETAILS
import com.training.ecommerce.utils.UserDetailsException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val appPreferencesRepository: AppPreferenceRepository,
    private val userPreferencesRepository: UserPreferenceRepository,
    private val userFirestoreRepository: UserFirestoreRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val logoutState = MutableSharedFlow<Resource<Unit>>()

    // load user data in state flow inside view model  scope
    val userDetailsState = getUserDetails().stateIn(
        viewModelScope, started = SharingStarted.Eagerly, initialValue = null
    )

    init {
        listenToUserDetails()
    }

    // load user data flow
    // we can use this to get user data in the view in main thread so we do not want to wait the data from state
    // note that this flow block the main thread while you get the data every time you call it
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserDetails() =
        userPreferencesRepository.getUserDetails().mapLatest { it.toUserDetailsModel() }

    private fun listenToUserDetails() = viewModelScope.launch {
        val userId = userPreferencesRepository.getUserId().first()
        if (userId.isEmpty()) return@launch
        userFirestoreRepository.getUserDetails(userId).catch { e ->
            val msg = e.message ?: "Error listening to user details"
            CrashlyticsUtils.sendCustomLogToCrashlytics<UserDetailsException>(
                msg, LISTEN_TO_USER_DETAILS to msg
            )
            if (e is UserDetailsException) logOut()
        }.collectLatest { resource ->
            Log.d(TAG, "listenToUserDetails: ${resource.data}")
            when (resource) {
                is Resource.Success -> {

                    resource.data?.let {
                        userPreferencesRepository.updateUserDetails(it.toUserDetailsPreferences())
                    }
                }

                else -> {
                    // Do nothing
                    Log.d(TAG, "Error listen to user details: ${resource.exception?.message}")
                }
            }
        }
    }

    suspend fun isUserLoggedIn() = appPreferencesRepository.isLoggedIn()
    suspend fun logOut() = viewModelScope.launch {
        logoutState.emit(Resource.Loading())
        firebaseAuthRepository.logout()
        userPreferencesRepository.clearUserPreferences()
        appPreferencesRepository.saveLoginState(false)
        logoutState.emit(Resource.Success(Unit))
    }

    companion object {
        private const val TAG = "UserViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: UserViewModel cleared")
    }
}

class UserViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    private val appPreferencesRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(context))
    private val userPreferencesRepository = UserPreferenceRepositoryImpl(context)
    private val userFirestoreRepository = UserFirestoreRepositoryImpl()
    private val firebaseAuthRepository = FirebaseAuthRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserViewModel(
                appPreferencesRepository,
                userPreferencesRepository,
                userFirestoreRepository,
                firebaseAuthRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}