package com.training.ecommerce.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.training.ecommerce.R
import com.training.ecommerce.ui.login.viewmodel.AuthViewModel

class AuthActivity : AppCompatActivity() {

    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}