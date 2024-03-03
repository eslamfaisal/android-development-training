package com.training.ecommerce.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.training.ecommerce.data.repository.user.UserDataStoreRepositoryImpl
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserViewModel(
    private val userPreferencesRepository: UserDataStoreRepositoryImpl
) : ViewModel() {


    suspend fun isUserLoggedIn() = userPreferencesRepository.isUserLoggedIn()
    fun setIsLoggedIn(b: Boolean) {
        viewModelScope.launch(IO) {
            userPreferencesRepository.saveLoginState(b)
        }
    }

}

class UserViewModelFactory(private val userPreferencesRepository: UserDataStoreRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}