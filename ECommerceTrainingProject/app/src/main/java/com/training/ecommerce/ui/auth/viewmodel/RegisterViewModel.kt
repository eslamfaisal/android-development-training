package com.training.ecommerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.models.auth.RegisterRequestModel
import com.training.ecommerce.data.models.auth.RegisterResponseModel
import com.training.ecommerce.data.models.user.UserDetailsModel
import com.training.ecommerce.data.repository.auth.FirebaseAuthRepository
import com.training.ecommerce.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _registerState = MutableSharedFlow<Resource<RegisterResponseModel>>()
    val registerState: SharedFlow<Resource<RegisterResponseModel>> = _registerState.asSharedFlow()

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    private val isRegisterIsValid = combine(
        name, email, password, confirmPassword
    ) { name, email, password, confirmPassword ->
        email.isValidEmail() && password.length >= 6 && name.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    }

    fun registerWithEmailAndPassword() = viewModelScope.launch(IO) {
        val name = name.value
        val email = email.value
        val password = password.value
        if (isRegisterIsValid.first()) {
            // handle register flow
            val registerResponseModel = RegisterRequestModel(
                fullName = name,
                email = email,
                password = password
            )
            authRepository.registerEmailAndPasswordWithAPI(registerResponseModel).collect {
                _registerState.emit(it)
            }
        } else {
            // emit error
        }
    }

    fun signUpWithGoogle(idToken: String) = viewModelScope.launch {
//        authRepository.registerWithGoogle(idToken).collect {
//            _registerState.emit(it)
//        }
    }

    fun registerWithFacebook(token: String) = viewModelScope.launch {
//        authRepository.registerWithFacebook(token).collect {
//            _registerState.emit(it)
//        }
    }
}