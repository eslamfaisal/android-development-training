package com.training.ecommerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.training.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.training.ecommerce.data.repository.user.UserPreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPrefs: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    val emailString : String = ""

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")


    fun login() {
        viewModelScope.launch {
            val email = email.value
            val password = password.value
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // call loginWithEmailAndPassword from authRepository
                // and pass email and password as parameters
                // collect the result and save the user id in userPrefs
                // and navigate to home screen
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