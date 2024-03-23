package com.training.ecommerce.ui.auth.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.training.ecommerce.data.datasource.datastore.UserPreferencesDataSource
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.training.ecommerce.data.repository.user.UserDataStoreRepositoryImpl
import com.training.ecommerce.databinding.FragmentLoginBinding
import com.training.ecommerce.ui.auth.viewmodel.LoginViewModel
import com.training.ecommerce.ui.auth.viewmodel.LoginViewModelFactory
import com.training.ecommerce.ui.common.views.ProgressDialog
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            userPrefs = UserDataStoreRepositoryImpl(
                UserPreferencesDataSource(
                    requireActivity()
                )
            ), authRepository = FirebaseAuthRepositoryImpl()
        )
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = loginViewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViewModel()

        Log.d(TAG, "onViewCreated: ")
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect { resource ->
                Log.d(TAG, "initViewModel: $resource")
                when (resource) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()
                        Toast.makeText(
                            requireContext(), "Login successfully", Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        Log.d(TAG, "Resource.Error: ${resource.exception?.message}")
                        Toast.makeText(
                            requireContext(), resource.exception?.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.loginBtn.setOnClickListener {
            loginViewModel.login()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val TAG = "LoginFragment"
    }
}