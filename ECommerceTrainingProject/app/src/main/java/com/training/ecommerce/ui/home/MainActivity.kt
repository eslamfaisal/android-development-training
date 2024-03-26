package com.training.ecommerce.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.training.ecommerce.R
import com.training.ecommerce.ui.auth.AuthActivity
import com.training.ecommerce.ui.common.viewmodel.UserViewModel
import com.training.ecommerce.ui.common.viewmodel.UserViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)
        val isLoggedIn = runBlocking { userViewModel.isUserLoggedIn().first() }
        if (!isLoggedIn) {
            goToAuthActivity()
            return
        }

        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.textView).setOnClickListener {
            logOut()
        }

        initViewModel()
    }

    private fun initViewModel() {
        userViewModel.listenToUserDetails()
        lifecycleScope.launch {
            val userDetails = runBlocking { userViewModel.getUserPrefsDetails().first() }
            Log.d(TAG, "initViewModel: user details ${userDetails.email}")

            userViewModel.userPrefsState.collect {
                Log.d(TAG, "initViewModel: user details updated ${it?.email}")
            }

        }
    }

    private fun logOut() {
        lifecycleScope.launch {
            userViewModel.logOut()
            goToAuthActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val options = ActivityOptions.makeCustomAnimation(
            this, android.R.anim.fade_in, android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun initSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        } else {
            setTheme(R.style.Theme_ECommerce)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}