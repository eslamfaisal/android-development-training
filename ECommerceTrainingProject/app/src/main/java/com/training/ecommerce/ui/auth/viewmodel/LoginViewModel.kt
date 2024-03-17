package com.training.ecommerce.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.training.ecommerce.data.repository.user.UserPreferenceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    val userPrefs: UserPreferenceRepository
) : ViewModel() {


    fun getFakeData() {
        viewModelScope.launch {
            delay(5000)
            Log.d(TAG, "getFakeData: ")
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

// create viewmodel factory class
class LoginViewModelFactory(
    private val userPrefs: UserPreferenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(userPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}