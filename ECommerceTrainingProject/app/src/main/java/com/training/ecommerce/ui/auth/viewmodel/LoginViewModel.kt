package com.training.ecommerce.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.training.ecommerce.data.repository.user.UserPreferenceRepository
import com.training.ecommerce.utils.isValidEmail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPrefs: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    val loginState: MutableStateFlow<Resource<String>?> = MutableStateFlow(null)

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val isLoginIsValid: Flow<Boolean> = combine(email, password) { email, password ->
        email.isValidEmail() && password.length >= 6
    }

    fun login() {
        viewModelScope.launch {
            val email = email.value
            val password = password.value
            if (isLoginIsValid.first()) {
                authRepository.loginWithEmailAndPassword(email, password).onEach { resource ->
                    Log.d(TAG, "Emitted resource: $resource")
                    when (resource) {
                        is Resource.Loading -> loginState.update { Resource.Loading() }
                        is Resource.Success -> {
                            delay(40000)
//                            userPrefs.saveUserEmail(email)
                            loginState.update { Resource.Success(resource.data ?: "Empty User Id") }
                        }

                        is Resource.Error -> loginState.value =
                            Resource.Error(resource.exception ?: Exception("Unknown error"))
                    }
                }.launchIn(viewModelScope)
            } else {
                loginState.update { Resource.Error(Exception("Invalid email or password")) }
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

// create viewmodel factory class
class LoginViewModelFactory(
    private val userPrefs: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(userPrefs, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}