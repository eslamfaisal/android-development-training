package com.training.ecommerce.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel: ViewModel() {

    val useId = MutableStateFlow("")
}