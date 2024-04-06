package com.training.ecommerce.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.training.ecommerce.R
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.data.repository.auth.FirebaseAuthRepositoryImpl
import com.training.ecommerce.databinding.FragmentRegisterBinding
import com.training.ecommerce.ui.auth.viewmodel.RegisterViewModel
import com.training.ecommerce.ui.auth.viewmodel.RegisterViewModelFactory
import com.training.ecommerce.ui.common.views.ProgressDialog
import com.training.ecommerce.ui.showSnakeBarError
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(FirebaseAuthRepositoryImpl())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = registerViewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initListener()
    }

    private fun initListener() {
        binding.btnSignIn.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), "${resource.data}", Toast.LENGTH_SHORT)
                            .show()
                        goToLogin()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                    }

                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val TAG = "RegisterFragment"
    }
}