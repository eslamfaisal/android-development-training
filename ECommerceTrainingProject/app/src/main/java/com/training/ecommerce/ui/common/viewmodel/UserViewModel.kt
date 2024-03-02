package com.training.ecommerce.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.training.ecommerce.data.repository.user.UserPreferencesRepository

class UserViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val isUserLoggedIn = userPreferencesRepository.isUserLoggedIn
    suspend fun saveLoginState(isLoggedIn: Boolean) {
        userPreferencesRepository.saveLoginState(isLoggedIn)
    }

}

class UserViewModelFactory(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}