package com.training.ecommerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.training.ecommerce.utils.isValidEmail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val auth: FirebaseAuthRepository
) : ViewModel() {
    private val _registerState = MutableSharedFlow<Resource<String>>()
    val registerState = _registerState.asSharedFlow()

    val password = MutableStateFlow("")
    val email = MutableStateFlow("")
    val fullName = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")


    private val isRegisterValid: Flow<Boolean> = combine(
        email,
        password,
        fullName,
    ) { email, password, fullName ->
        email.isValidEmail() && password.length >= 6 && fullName.isNotEmpty()
    }


    fun register() = viewModelScope.launch {
        val email = email.value
        val password = password.value
        val fullName = fullName.value
        if (isRegisterValid.first()) {
            if (password == confirmPassword.value) {
                _registerState.emit(Resource.Loading())
                auth.registerWithEmailAndPassword(email, password, fullName)
                    .collect { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                _registerState.emit(Resource.Success(resource.data!!))
                            }

                            else -> {
                                _registerState.emit(resource)
                            }
                        }
                    }

            } else {
                _registerState.emit(Resource.Error(Exception("Passwords do not match")))
            }

        } else {
            _registerState.emit(Resource.Error(Exception("Invalid email or password")))
        }


    }

}

class RegisterViewModelFactory(
    private val auth: FirebaseAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return RegisterViewModel(auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
