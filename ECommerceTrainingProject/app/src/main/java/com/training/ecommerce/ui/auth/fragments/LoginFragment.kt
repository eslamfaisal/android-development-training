package com.training.ecommerce.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.training.ecommerce.R
import com.training.ecommerce.data.datasource.datastore.UserPreferencesDataSource
import com.training.ecommerce.data.repository.user.UserDataStoreRepositoryImpl
import com.training.ecommerce.databinding.FragmentLoginBinding
import com.training.ecommerce.ui.auth.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    val loginViewModel: LoginViewModel by lazy {
        LoginViewModel(
            userPrefs = UserDataStoreRepositoryImpl(
                UserPreferencesDataSource(
                    requireActivity()
                )
            )
        )
    }

    lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    companion object {

        private const val TAG = "LoginFragment"
    }
}