package com.training.ecommerce.ui.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import com.training.ecommerce.utils.isValidEmail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(
    private val appPreferenceRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
    private val userFirestoreRepository: UserFirestoreRepository
) : ViewModel() {

    private val _loginState = MutableSharedFlow<Resource<String>>()
    val loginState: SharedFlow<Resource<String>> = _loginState.asSharedFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val isLoginIsValid: Flow<Boolean> = combine(email, password) { email, password ->
        email.isValidEmail() && password.length >= 6
    }

    fun login() = viewModelScope.launch {
        val email = email.value
        val password = password.value
        if (isLoginIsValid.first()) {
            authRepository.loginWithEmailAndPassword(email, password).onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        savePreferenceData(resource.data!!)
                        _loginState.emit(Resource.Success(resource.data))
                    }

                    else -> _loginState.emit(resource)
                }
            }.launchIn(viewModelScope)
        } else {
            _loginState.emit(Resource.Error(Exception("Invalid email or password")))
        }
    }

    private suspend fun savePreferenceData(userID: String) {
        appPreferenceRepository.saveLoginState(true)
        userPreferenceRepository.updateUserId(userID)
    }

    fun loginWithGoogle(idToken: String) = viewModelScope.launch {
        authRepository.loginWithGoogle(idToken).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    savePreferenceData(resource.data!!)
                    _loginState.emit(Resource.Success(resource.data))
                }

                else -> _loginState.emit(resource)
            }
        }.launchIn(viewModelScope)
    }


    companion object {
        private const val TAG = "LoginViewModel"
    }
}

// create viewmodel factory class
class LoginViewModelFactory(
    private val contextValue: Context
) : ViewModelProvider.Factory {

    private val appPreferenceRepository: AppPreferenceRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(contextValue))
    private val userPreferenceRepository: UserPreferenceRepository =
        UserPreferenceRepositoryImpl(contextValue)
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepositoryImpl()
    private val userFirestoreRepository: UserFirestoreRepository = UserFirestoreRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(
                appPreferenceRepository,
                userPreferenceRepository,
                authRepository,
                userFirestoreRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}